package com.wasd.website.controller;

import com.wasd.website.model.user.request.CreateUserRequest;
import com.wasd.website.model.user.response.UserResponse;
import com.wasd.website.service.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{username}")
    public UserResponse findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }
    
    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }
}
