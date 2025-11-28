package com.example.serveez.dto.request;

import com.example.serveez.model.Notification;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificationRequest {
    @NotBlank(message = "Target type is required")
    private String targetType; // ALL_USERS, ALL_PROVIDERS, USER, PROVIDER, ALL

    private String targetUserId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    private Notification.NotificationType type = Notification.NotificationType.ADMIN_ANNOUNCEMENT;
}
