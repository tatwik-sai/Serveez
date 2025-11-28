package com.example.serveez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingResponse {
    private String id;
    private String userId;
    private String providerId;
    private String serviceListingId;
    private String status;
    private LocalDateTime scheduledAt;
    private String notes;
    private Double priceAtBooking;
    private LocalDateTime createdAt;
}
