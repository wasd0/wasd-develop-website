package com.wasd.website.service.user.impl;

import com.wasd.website.entity.Role;
import com.wasd.website.entity.User;
import com.wasd.website.model.user.request.UserRequest;
import com.wasd.website.model.user.response.UserResponse;
import com.wasd.website.repository.UserRepository;
import com.wasd.website.service.user.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);

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
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        return mapUserToResponse(user);
    }

    @Override
    public UserResponse create(UserRequest request) throws EntityExistsException {
        String username = request.getUsername();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new EntityExistsException(String.format("User with username '%s' already exists!",
                    username));
        }

        User user = mapCreateRequestToUser(request);
        userRepository.save(user);
        return mapUserToResponse(user);
    }

    @Override
    public void delete(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);
    }

    @Override
    public UserResponse update(String username, UserRequest request) throws EntityExistsException {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);

        if (!user.getUsername().equals(request.getUsername()) 
                && userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new EntityExistsException("User with username '%s' already exists!");
        }

        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());

        userRepository.save(user);

        return mapUserToResponse(user);
    }

    private User mapCreateRequestToUser(UserRequest request) {
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

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getAuthorities().forEach(authority ->
                    authorities.add(new SimpleGrantedAuthority(authority.getName())));
        });

        return authorities;
    }
}
