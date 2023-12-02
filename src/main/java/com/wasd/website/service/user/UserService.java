package com.wasd.website.service.user;

import com.wasd.website.model.user.request.UserRequest;
import com.wasd.website.model.user.response.UserResponse;
import jakarta.persistence.EntityExistsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public interface UserService {
    Collection<UserResponse> findAll();

    UserResponse findByUsername(String username) throws UsernameNotFoundException;

    UserResponse create(UserRequest request);

    void delete(String username);
    
    UserResponse update(String username, UserRequest request) throws EntityExistsException;
}
