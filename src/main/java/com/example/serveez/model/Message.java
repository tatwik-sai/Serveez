package com.example.serveez.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    @Indexed
    private String fromUserId;

    @Indexed
    private String toUserId;

    @Indexed
    private String bookingId;

    private String serviceListingId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;
}
