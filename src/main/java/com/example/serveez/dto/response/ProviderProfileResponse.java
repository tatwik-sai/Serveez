package com.example.serveez.dto.response;

import com.example.serveez.model.ProviderProfile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProviderProfileResponse {
    private String id;
    private String userId;
    private String displayName;
    private String bio;
    private ProviderProfile.Location location;
    private String phone;
    private String additionalContacts;
    private Double averageRating;
    private Integer totalReviews;
    private LocalDateTime createdAt;
}
