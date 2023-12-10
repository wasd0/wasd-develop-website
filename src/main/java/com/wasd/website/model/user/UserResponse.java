package com.wasd.website.model.user;

import java.time.LocalDateTime;

public record UserResponse(Long id, String username, String email, LocalDateTime registrationDate) {
}
