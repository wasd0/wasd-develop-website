package com.wasd.website.controller;

import com.wasd.website.model.post.PostRequest;
import com.wasd.website.model.post.PostResponse;
import com.wasd.website.service.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {
    
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<PostResponse> findAll() {
        return postService.findAll();
    }
    
    @GetMapping("/author/{username}")
    public Collection<PostResponse> findByAuthor(@PathVariable String username) {
        return postService.findByAuthor(username);
    }
    
    @GetMapping("{id}")
    public PostResponse findById(@PathVariable Long id) {
        return postService.findById(id);
    }
    
    @PostMapping
    public PostResponse create(@RequestBody PostRequest request, Principal principal) {
        return postService.create(request, principal);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Principal principal) {
        postService.delete(id, principal);
    }
    
    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id, @RequestBody PostRequest request, Principal principal) {
        return postService.update(id, request, principal);
    }
}
