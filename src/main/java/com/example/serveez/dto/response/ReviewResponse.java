package com.example.serveez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String bookingId;
    private String userId;
    private String providerId;
    private String serviceListingId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
