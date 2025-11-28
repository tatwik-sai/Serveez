package com.example.serveez.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequest {
    @NotBlank(message = "To user ID is required")
    private String toUserId;

    private String bookingId;

    private String serviceListingId;

    @NotBlank(message = "Content is required")
    private String content;
}
