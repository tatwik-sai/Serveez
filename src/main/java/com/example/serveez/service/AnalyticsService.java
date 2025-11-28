package com.example.serveez.service;

import com.example.serveez.dto.response.*;
import com.example.serveez.model.Booking;
import com.example.serveez.model.User;
import com.example.serveez.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserRepository userRepository;
    private final ServiceListingRepository listingRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * Get summary metrics for the platform
     */
    public AnalyticsSummaryResponse getSummaryMetrics() {
        // Count users by role
        long totalUsers = userRepository.findAll().stream()
                .filter(user -> user.getRole() == User.UserRole.USER)
                .count();

        long totalProviders = userRepository.findAll().stream()
                .filter(user -> user.getRole() == User.UserRole.PROVIDER)
                .count();

        // Count active listings
        long totalActiveListings = listingRepository.findByIsActiveTrue().size();

        // Total bookings
        long totalBookings = bookingRepository.count();

        // Bookings by status
        Map<String, Long> bookingsByStatus = Arrays.stream(Booking.BookingStatus.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        status -> (long) bookingRepository.findByStatus(status).size()));

        // Average rating overall
        Double averageRating = reviewRepository.findAll().stream()
                .mapToInt(review -> review.getRating())
                .average()
                .orElse(0.0);

        return new AnalyticsSummaryResponse(
                totalUsers,
                totalProviders,
                totalActiveListings,
                totalBookings,
                bookingsByStatus,
                Math.round(averageRating * 100.0) / 100.0);
    }

    /**
     * Get bookings over time (grouped by day)
     */
    public BookingsOverTimeResponse getBookingsOverTime(Integer days) {
        if (days == null || days <= 0) {
            days = 30;
        }

        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        // Get all bookings within the range
        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(booking -> booking.getCreatedAt().isAfter(startDate))
                .collect(Collectors.toList());

        // Group by date
        Map<String, Long> bookingsByDate = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getCreatedAt().toLocalDate().toString(),
                        Collectors.counting()));

        // Create time points for all days in range (including days with 0 bookings)
        List<BookingsOverTimeResponse.TimePoint> points = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateStr = date.format(formatter);
            Long count = bookingsByDate.getOrDefault(dateStr, 0L);
            points.add(new BookingsOverTimeResponse.TimePoint(dateStr, count));
        }

        return new BookingsOverTimeResponse(days, points);
    }

    /**
     * Get top categories by booking count
     */
    public List<TopCategoryResponse> getTopCategories(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 5;
        }

        // Get all bookings with their listing info
        Map<String, Long> categoryBookingCount = new HashMap<>();

        bookingRepository.findAll().forEach(booking -> {
            listingRepository.findById(booking.getServiceListingId())
                    .ifPresent(listing -> {
                        String categoryId = listing.getCategoryId();
                        categoryBookingCount.put(categoryId,
                                categoryBookingCount.getOrDefault(categoryId, 0L) + 1);
                    });
        });

        // Sort and limit
        return categoryBookingCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    String categoryId = entry.getKey();
                    String categoryName = categoryRepository.findById(categoryId)
                            .map(category -> category.getName())
                            .orElse("Unknown");
                    return new TopCategoryResponse(categoryId, categoryName, entry.getValue());
                })
                .collect(Collectors.toList());
    }

    /**
     * Get top providers by completed bookings and rating
     */
    public List<TopProviderResponse> getTopProviders(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 5;
        }

        // Get completed bookings count per provider
        Map<String, Long> providerCompletedBookings = bookingRepository
                .findByStatus(Booking.BookingStatus.COMPLETED)
                .stream()
                .collect(Collectors.groupingBy(
                        Booking::getProviderId,
                        Collectors.counting()));

        // Get average rating per provider
        Map<String, Double> providerRatings = new HashMap<>();
        reviewRepository.findAll().forEach(review -> {
            String providerId = review.getProviderId();
            providerRatings.putIfAbsent(providerId, 0.0);
        });

        // Calculate averages
        reviewRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        review -> review.getProviderId(),
                        Collectors.averagingInt(review -> review.getRating())))
                .forEach(providerRatings::put);

        // Combine data and sort
        return providerCompletedBookings.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    String providerId = entry.getKey();
                    String providerName = providerProfileRepository.findByUserId(providerId)
                            .map(profile -> profile.getDisplayName())
                            .orElse("Unknown Provider");
                    Long completedCount = entry.getValue();
                    Double avgRating = providerRatings.getOrDefault(providerId, 0.0);
                    avgRating = Math.round(avgRating * 100.0) / 100.0;

                    return new TopProviderResponse(providerId, providerName, completedCount, avgRating);
                })
                .collect(Collectors.toList());
    }
}
