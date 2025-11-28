package com.example.serveez.controller;

import com.example.serveez.dto.request.NotificationRequest;
import com.example.serveez.dto.response.ApiResponse;
import com.example.serveez.dto.response.NotificationResponse;
import com.example.serveez.security.UserPrincipal;
import com.example.serveez.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get notifications for current user
     * GET /api/notifications?isRead=false&page=0&size=20
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getNotifications(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<NotificationResponse> notifications = notificationService.getNotificationsForUser(
                currentUser.getId(), isRead, page, size);

        long unreadCount = notificationService.getUnreadCount(currentUser.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("notifications", notifications.getContent());
        response.put("currentPage", notifications.getNumber());
        response.put("totalItems", notifications.getTotalElements());
        response.put("totalPages", notifications.getTotalPages());
        response.put("unreadCount", unreadCount);

        return ResponseEntity.ok(response);
    }

    /**
     * Mark a single notification as read
     * POST /api/notifications/{id}/read
     */
    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id) {

        NotificationResponse notification = notificationService.markAsRead(id, currentUser.getId());
        return ResponseEntity.ok(notification);
    }

    /**
     * Mark all notifications as read for current user
     * POST /api/notifications/read-all
     */
    @PostMapping("/read-all")
    public ResponseEntity<ApiResponse> markAllAsRead(
            @AuthenticationPrincipal UserPrincipal currentUser) {

        notificationService.markAllAsRead(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse(true, "All notifications marked as read"));
    }

    /**
     * Admin: Send targeted or broadcast notification
     * POST /api/admin/notifications
     */
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> sendAdminNotification(
            @Valid @RequestBody NotificationRequest request) {

        notificationService.sendAdminNotification(request);
        return ResponseEntity.ok(new ApiResponse(true, "Notification sent successfully"));
    }
}
