package com.wasd.website.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MainController {
    
    @GetMapping
    public String homePage() {
        return "home page";
    }
    
    @GetMapping("/authenticated")
    public String authenticated() {
        return "authenticated part of site";
    }
    
    @GetMapping("/admin")
    public String admin() {
        return "admin part of site";
    }
}
