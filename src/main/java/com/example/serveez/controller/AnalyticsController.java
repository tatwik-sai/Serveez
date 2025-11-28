package com.example.serveez.controller;

import com.example.serveez.dto.response.*;
import com.example.serveez.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/analytics")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Get overall platform summary metrics
     * GET /api/admin/analytics/summary
     */
    @GetMapping("/summary")
    public ResponseEntity<AnalyticsSummaryResponse> getSummary() {
        return ResponseEntity.ok(analyticsService.getSummaryMetrics());
    }

    /**
     * Get bookings over time (grouped by day)
     * GET /api/admin/analytics/bookings-over-time?days=30
     */
    @GetMapping("/bookings-over-time")
    public ResponseEntity<BookingsOverTimeResponse> getBookingsOverTime(
            @RequestParam(defaultValue = "30") Integer days) {
        return ResponseEntity.ok(analyticsService.getBookingsOverTime(days));
    }

    /**
     * Get top categories by booking count
     * GET /api/admin/analytics/top-categories?limit=5
     */
    @GetMapping("/top-categories")
    public ResponseEntity<List<TopCategoryResponse>> getTopCategories(
            @RequestParam(defaultValue = "5") Integer limit) {
        return ResponseEntity.ok(analyticsService.getTopCategories(limit));
    }

    /**
     * Get top providers by completed bookings and rating
     * GET /api/admin/analytics/top-providers?limit=5
     */
    @GetMapping("/top-providers")
    public ResponseEntity<List<TopProviderResponse>> getTopProviders(
            @RequestParam(defaultValue = "5") Integer limit) {
        return ResponseEntity.ok(analyticsService.getTopProviders(limit));
    }
}
