package com.example.serveez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String id;
    private String fromUserId;
    private String toUserId;
    private String bookingId;
    private String serviceListingId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}
