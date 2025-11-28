package com.example.serveez.service;

import com.example.serveez.dto.request.ReviewRequest;
import com.example.serveez.dto.response.ReviewResponse;
import com.example.serveez.exception.BadRequestException;
import com.example.serveez.exception.ResourceNotFoundException;
import com.example.serveez.exception.UnauthorizedException;
import com.example.serveez.model.Booking;
import com.example.serveez.model.Review;
import com.example.serveez.repository.BookingRepository;
import com.example.serveez.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ProviderProfileService providerProfileService;
    private final NotificationService notificationService;

    public ReviewResponse createReview(String userId, String bookingId, ReviewRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only review your own bookings");
        }

        if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
            throw new BadRequestException("You can only review completed bookings");
        }

        if (reviewRepository.existsByBookingId(bookingId)) {
            throw new BadRequestException("Review already exists for this booking");
        }

        Review review = new Review();
        review.setBookingId(bookingId);
        review.setUserId(userId);
        review.setProviderId(booking.getProviderId());
        review.setServiceListingId(booking.getServiceListingId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        review = reviewRepository.save(review);

        // Update provider average rating
        updateProviderRating(booking.getProviderId());

        // Notify provider about new review
        notificationService.createNotification(
                booking.getProviderId(),
                com.example.serveez.model.Notification.NotificationType.REVIEW_RECEIVED,
                "New Review",
                String.format("You received a %d-star review", request.getRating()),
                bookingId,
                booking.getServiceListingId(),
                userId,
                null);

        return mapToResponse(review);
    }

    public ReviewResponse updateReview(String userId, String reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only update your own reviews");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        review = reviewRepository.save(review);

        // Update provider average rating
        updateProviderRating(review.getProviderId());

        return mapToResponse(review);
    }

    public void deleteReview(String userId, String reviewId, boolean isAdmin) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!isAdmin && !review.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own reviews");
        }

        String providerId = review.getProviderId();
        reviewRepository.delete(review);

        // Update provider average rating
        updateProviderRating(providerId);
    }

    public List<ReviewResponse> getListingReviews(String listingId) {
        return reviewRepository.findByServiceListingId(listingId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getProviderReviews(String providerId) {
        return reviewRepository.findByProviderId(providerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void updateProviderRating(String providerId) {
        List<Review> reviews = reviewRepository.findByProviderId(providerId);
        if (reviews.isEmpty()) {
            providerProfileService.updateRating(providerId, 0.0, 0);
        } else {
            double averageRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            providerProfileService.updateRating(providerId, averageRating, reviews.size());
        }
    }

    private ReviewResponse mapToResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getBookingId(),
                review.getUserId(),
                review.getProviderId(),
                review.getServiceListingId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt());
    }
}
