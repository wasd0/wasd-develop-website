package com.wasd.website.model.post;

import com.wasd.website.model.user.UserResponse;

import java.time.LocalDateTime;

public record PostResponse(Long id, String title, String content, UserResponse author,
                           LocalDateTime creationTime) {
}
