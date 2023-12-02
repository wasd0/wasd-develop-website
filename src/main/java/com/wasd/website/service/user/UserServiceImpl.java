package com.wasd.website.service.user;

import com.wasd.website.entity.Role;
import com.wasd.website.entity.User;
import com.wasd.website.model.user.request.CreateUserRequest;
import com.wasd.website.model.user.response.UserResponse;
import com.wasd.website.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = tryFindUserByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    @Override
    public Collection<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse findByUsername(String username) throws UsernameNotFoundException {
        User user = tryFindUserByUsername(username);
        return mapUserToResponse(user);
    }

    @Override
    public UserResponse create(CreateUserRequest request) throws EntityExistsException {
        String username = request.getUsername();

        if (userRepository.findByUsername(username) != null) {
            throw new EntityExistsException(String.format("User with username '%s' already exists!",
                    username));
        }

        User user = mapCreateRequestToUser(request);
        userRepository.save(user);
        return mapUserToResponse(user);
    }

    private User tryFindUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with username '%s' not found.", username));
        }
        
        return user;
    }
    
    private User mapCreateRequestToUser(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        return user;
    }

    private UserResponse mapUserToResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
                user.getRegistrationDate());
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
