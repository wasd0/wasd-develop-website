package com.wasd.website.service.user.impl;

import com.wasd.website.entity.Role;
import com.wasd.website.entity.User;
import com.wasd.website.model.user.UserRequest;
import com.wasd.website.model.user.UserResponse;
import com.wasd.website.model.user.UserRole;
import com.wasd.website.repository.UserRepository;
import com.wasd.website.service.role.RoleService;
import com.wasd.website.service.user.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username '%s' not found", username)));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    @Override
    @Transactional
    public Collection<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse findByUsername(String username) throws EntityNotFoundException {
        User user = getUserOrElseThrowException(username);
        
        return mapUserToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse create(UserRequest request) throws EntityExistsException {
        String username = request.username();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new EntityExistsException(String.format("User with username '%s' already exists!",
                    username));
        }

        User user = createNewUserFromRequest(request);
        userRepository.save(user);
        return mapUserToResponse(user);
    }

    @Override
    public void delete(String username) {
        User user = getUserOrElseThrowException(username);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserResponse update(String username, UserRequest request) throws EntityExistsException {
        User user = getUserOrElseThrowException(username);

        if (!user.getUsername().equals(request.username())
                && userRepository.findByUsername(request.username()).isPresent()) {
            throw new EntityExistsException("User with username '%s' already exists!");
        }

        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setUsername(request.username());

        userRepository.save(user);

        return mapUserToResponse(user);
    }

    private User createNewUserFromRequest(UserRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setRegistrationDate(LocalDateTime.now());
        user.setRoles(Set.of(roleService.getUserRole(UserRole.USER)));

        return user;
    }

    private UserResponse mapUserToResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
                user.getRegistrationDate());
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getAuthorities().forEach(authority ->
                    authorities.add(new SimpleGrantedAuthority(authority.getName())));
        });

        return authorities;
    }

    private User getUserOrElseThrowException(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(
                String.format("User with username '%s' not found", username)));
    }
}
