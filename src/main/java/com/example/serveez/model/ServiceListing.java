package com.example.serveez.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "service_listings")
public class ServiceListing {
    @Id
    private String id;

    @Indexed
    private String providerId;

    @Indexed
    private String categoryId;

    private String title;

    private String description;

    private Double price;

    private String location;

    private Integer estimatedDuration; // in minutes

    private List<Photo> photos = new ArrayList<>();

    private Boolean isActive = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Data
    public static class Photo {
        private String id;
        private String url;
        private String caption;
        private LocalDateTime uploadedAt;
    }
}
