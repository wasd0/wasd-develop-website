package com.wasd.website.controller;

import com.wasd.website.model.user.UserRequest;
import com.wasd.website.model.user.UserResponse;
import com.wasd.website.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public UserResponse create(@RequestBody UserRequest request) {
        return userService.create(request);
    }
    
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String username) {
        userService.delete(username);
    }
    
    @PatchMapping
    public UserResponse update(Principal principal, @RequestBody UserRequest request) {
        return userService.update(principal.getName(), request);
    }
}
