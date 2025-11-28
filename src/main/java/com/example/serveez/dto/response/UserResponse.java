package com.example.serveez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
