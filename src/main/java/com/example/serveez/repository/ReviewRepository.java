package com.example.serveez.repository;

import com.example.serveez.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Optional<Review> findByBookingId(String bookingId);

    List<Review> findByServiceListingId(String serviceListingId);

    List<Review> findByProviderId(String providerId);

    List<Review> findByUserId(String userId);

    boolean existsByBookingId(String bookingId);
}
