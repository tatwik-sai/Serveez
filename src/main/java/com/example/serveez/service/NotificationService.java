package com.example.serveez.service;

import com.example.serveez.dto.request.NotificationRequest;
import com.example.serveez.dto.response.NotificationResponse;
import com.example.serveez.exception.ResourceNotFoundException;
import com.example.serveez.exception.UnauthorizedException;
import com.example.serveez.model.Notification;
import com.example.serveez.model.User;
import com.example.serveez.repository.NotificationRepository;
import com.example.serveez.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /**
     * Create a notification for a specific user
     */
    public void createNotification(String userId, Notification.NotificationType type,
            String title, String message,
            String relatedBookingId, String relatedListingId,
            String relatedUserId, Map<String, Object> metadata) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedBookingId(relatedBookingId);
        notification.setRelatedListingId(relatedListingId);
        notification.setRelatedUserId(relatedUserId);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        if (metadata != null) {
            notification.setMetadata(metadata);
        }

        notificationRepository.save(notification);
        log.info("Created notification for user {}: {}", userId, type);
    }

    /**
     * Get notifications for current user with optional filtering
     */
    public Page<NotificationResponse> getNotificationsForUser(String userId, Boolean isRead,
            int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications;

        if (isRead != null) {
            notifications = notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, isRead, pageable);
        } else {
            notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        }

        return notifications.map(this::mapToResponse);
    }

    /**
     * Mark a notification as read
     */
    public NotificationResponse markAsRead(String notificationId, String currentUserId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getUserId().equals(currentUserId)) {
            throw new UnauthorizedException("You can only mark your own notifications as read");
        }

        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notification = notificationRepository.save(notification);
        }

        return mapToResponse(notification);
    }

    /**
     * Mark all notifications as read for current user
     */
    public void markAllAsRead(String currentUserId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(currentUserId);

        LocalDateTime now = LocalDateTime.now();
        unreadNotifications.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(now);
        });

        notificationRepository.saveAll(unreadNotifications);
        log.info("Marked {} notifications as read for user {}", unreadNotifications.size(), currentUserId);
    }

    /**
     * Admin: Send notification to targeted users
     */
    public void sendAdminNotification(NotificationRequest request) {
        List<String> targetUserIds = determineTargetUsers(request);

        LocalDateTime now = LocalDateTime.now();
        List<Notification> notifications = targetUserIds.stream()
                .map(userId -> {
                    Notification notification = new Notification();
                    notification.setUserId(userId);
                    notification.setType(request.getType());
                    notification.setTitle(request.getTitle());
                    notification.setMessage(request.getMessage());
                    notification.setIsRead(false);
                    notification.setCreatedAt(now);
                    return notification;
                })
                .collect(Collectors.toList());

        notificationRepository.saveAll(notifications);
        log.info("Admin sent {} notifications to target: {}", notifications.size(), request.getTargetType());
    }

    /**
     * Get unread notification count for user
     */
    public long getUnreadCount(String userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    private List<String> determineTargetUsers(NotificationRequest request) {
        String targetType = request.getTargetType();

        return switch (targetType) {
            case "ALL" -> userRepository.findAll().stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
            case "ALL_USERS" -> userRepository.findAll().stream()
                    .filter(user -> user.getRole() == User.UserRole.USER)
                    .map(User::getId)
                    .collect(Collectors.toList());
            case "ALL_PROVIDERS" -> userRepository.findAll().stream()
                    .filter(user -> user.getRole() == User.UserRole.PROVIDER)
                    .map(User::getId)
                    .collect(Collectors.toList());
            case "USER", "PROVIDER" -> {
                if (request.getTargetUserId() == null) {
                    throw new IllegalArgumentException("targetUserId is required for USER/PROVIDER target type");
                }
                yield List.of(request.getTargetUserId());
            }
            default -> throw new IllegalArgumentException("Invalid target type: " + targetType);
        };
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notification.getType().name(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getRelatedBookingId(),
                notification.getRelatedListingId(),
                notification.getRelatedUserId(),
                notification.getIsRead(),
                notification.getCreatedAt(),
                notification.getReadAt(),
                notification.getMetadata());
    }
}
