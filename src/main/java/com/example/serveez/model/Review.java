package com.example.serveez.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;

    @Indexed(unique = true)
    private String bookingId;

    @Indexed
    private String userId;

    @Indexed
    private String providerId;

    @Indexed
    private String serviceListingId;

    private Integer rating; // 1-5

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
