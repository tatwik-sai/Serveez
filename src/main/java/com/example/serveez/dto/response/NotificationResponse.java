package com.example.serveez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private String userId;
    private String type;
    private String title;
    private String message;
    private String relatedBookingId;
    private String relatedListingId;
    private String relatedUserId;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private Map<String, Object> metadata;
}
