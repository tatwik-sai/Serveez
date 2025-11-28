package com.example.serveez.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String providerId;

    @Indexed
    private String serviceListingId;

    @Indexed
    private BookingStatus status = BookingStatus.PENDING;

    private LocalDateTime scheduledAt;

    private String notes;

    private String cancellationReason;

    private Double priceAtBooking;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public enum BookingStatus {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }
}
