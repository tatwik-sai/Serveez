package com.example.serveez.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsSummaryResponse {
    private Long totalUsers;
    private Long totalProviders;
    private Long totalActiveListings;
    private Long totalBookings;
    private Map<String, Long> bookingsByStatus;
    private Double averageRatingOverall;
}
