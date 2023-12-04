package com.wasd.website.model.post.response;

import com.wasd.website.model.user.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private UserResponse authorResponse;
    private LocalDateTime creationTime;
}
