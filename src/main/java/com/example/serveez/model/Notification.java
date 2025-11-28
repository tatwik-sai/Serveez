package com.example.serveez.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;

    @Indexed
    private String userId;

    private NotificationType type;

    private String title;

    private String message;

    private String relatedBookingId;

    private String relatedListingId;

    private String relatedUserId;

    @Indexed
    private Boolean isRead = false;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    private Map<String, Object> metadata = new HashMap<>();

    public enum NotificationType {
        BOOKING_CREATED,
        BOOKING_CONFIRMED,
        BOOKING_COMPLETED,
        BOOKING_CANCELLED,
        BOOKING_CANCELLED_ADMIN,
        MESSAGE_RECEIVED,
        REVIEW_RECEIVED,
        ADMIN_ANNOUNCEMENT
    }
}
