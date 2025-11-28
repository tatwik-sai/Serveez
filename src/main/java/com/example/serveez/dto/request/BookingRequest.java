package com.example.serveez.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    @NotBlank(message = "Service listing ID is required")
    private String serviceListingId;

    private LocalDateTime scheduledAt;

    private String notes;
}
