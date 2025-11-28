package com.example.serveez.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "provider_profiles")
public class ProviderProfile {
    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    private String displayName;

    private String bio;

    private Location location;

    private String phone;

    private String additionalContacts;

    private Double averageRating = 0.0;

    private Integer totalReviews = 0;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Data
    public static class Location {
        private String city;
        private String area;
        private Double latitude;
        private Double longitude;
    }
}
