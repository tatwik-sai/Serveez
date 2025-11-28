package com.example.serveez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private String id;
    private String name;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
