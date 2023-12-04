package com.wasd.website.service.post;

import com.wasd.website.model.post.request.PostRequest;
import com.wasd.website.model.post.response.PostResponse;

import java.security.Principal;
import java.util.Collection;

public interface PostService {
    Collection<PostResponse> findAll();
    
    Collection<PostResponse> findByAuthor(String username);
    
    PostResponse findById(Long id);

    PostResponse create(PostRequest request, Principal principal);

    PostResponse update(Long id, PostRequest request, Principal principal);

    void delete(Long postId, Principal principal);
}
