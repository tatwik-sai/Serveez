package com.example.serveez.repository;

import com.example.serveez.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(String userId);

    List<Booking> findByProviderId(String providerId);

    List<Booking> findByServiceListingId(String serviceListingId);

    List<Booking> findByStatus(Booking.BookingStatus status);
}
