package com.example.serveez.controller;

import com.example.serveez.dto.request.ReviewRequest;
import com.example.serveez.dto.response.ApiResponse;
import com.example.serveez.dto.response.ReviewResponse;
import com.example.serveez.security.UserPrincipal;
import com.example.serveez.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // User endpoints
    @PostMapping("/api/bookings/{bookingId}/reviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String bookingId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(currentUser.getId(), bookingId, request));
    }

    @PutMapping("/api/reviews/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewResponse> updateReview(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(currentUser.getId(), id, request));
    }

    @DeleteMapping("/api/reviews/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> deleteReview(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id) {
        reviewService.deleteReview(currentUser.getId(), id, false);
        return ResponseEntity.ok(new ApiResponse(true, "Review deleted successfully"));
    }

    // Public endpoints
    @GetMapping("/api/listings/{listingId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getListingReviews(@PathVariable String listingId) {
        return ResponseEntity.ok(reviewService.getListingReviews(listingId));
    }

    @GetMapping("/api/providers/{providerId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getProviderReviews(@PathVariable String providerId) {
        return ResponseEntity.ok(reviewService.getProviderReviews(providerId));
    }

    // Admin endpoints
    @GetMapping("/api/admin/reviews")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @DeleteMapping("/api/admin/reviews/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> adminDeleteReview(@PathVariable String id) {
        reviewService.deleteReview(null, id, true);
        return ResponseEntity.ok(new ApiResponse(true, "Review deleted successfully"));
    }
}
