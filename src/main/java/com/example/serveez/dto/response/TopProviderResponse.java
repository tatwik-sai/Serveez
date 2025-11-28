package com.example.serveez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopProviderResponse {
    private String providerId;
    private String providerName;
    private Long completedBookings;
    private Double averageRating;
}
