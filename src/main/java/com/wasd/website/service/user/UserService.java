package com.wasd.website.service.user;

import com.wasd.website.model.user.request.CreateUserRequest;
import com.wasd.website.model.user.response.UserResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public interface UserService {
    Collection<UserResponse> findAll();
    UserResponse findByUsername(String username) throws UsernameNotFoundException;

    UserResponse create(CreateUserRequest request);
}
