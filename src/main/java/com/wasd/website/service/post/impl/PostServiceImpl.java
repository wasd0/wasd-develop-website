package com.wasd.website.service.post.impl;

import com.wasd.website.entity.Post;
import com.wasd.website.entity.User;
import com.wasd.website.model.post.request.PostRequest;
import com.wasd.website.model.post.response.PostResponse;
import com.wasd.website.model.user.response.UserResponse;
import com.wasd.website.repository.PostRepository;
import com.wasd.website.service.post.PostService;
import com.wasd.website.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Collection<PostResponse> findAll() {
        return postRepository.findAll().stream()
                .map(this::mapPostToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Collection<PostResponse> findByAuthor(String username) {
        UserResponse userResponse = userService.findByUsername(username);
        User user = createUserFromUserResponse(userResponse);

        return postRepository.findByAuthor(user).stream()
                .map(this::mapPostToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return mapPostToResponse(post);
    }

    @Override
    @Transactional
    public PostResponse create(PostRequest request, String principal) {
        if (principal.isEmpty()) {
            throw new EntityNotFoundException("Post creation exception: Cannot create post without authorization");
        }
        
        UserResponse user = userService.findByUsername(principal);
        Post post = mapRequestToPost(request);
        
        User author = new User();
        author.setUsername(user.getUsername());
        author.setId(user.getId());
        
        post.setAuthor(author);
        
        postRepository.save(post);

        return mapPostToResponse(post);
    }

    @Override
    @Transactional
    public PostResponse update(Long id, PostRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id, String principal) {
        Post post = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (!post.getAuthor().getUsername().equals(principal)) {
            throw new EntityNotFoundException();
        }

        postRepository.delete(post);
    }

    private Post mapRequestToPost(PostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        return post;
    }

    private PostResponse mapPostToResponse(Post post) {
        UserResponse userResponse = userService.findByUsername(post.getAuthor().getUsername());
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), userResponse,
                post.getCreationTime());
    }

    private User createUserFromUserResponse(UserResponse userResponse) {
        User user = new User();
        user.setUsername(userResponse.getUsername());
        user.setEmail(userResponse.getEmail());
        user.setRegistrationDate(userResponse.getRegistrationDate());
        user.setId(userResponse.getId());
        return user;
    }
}
