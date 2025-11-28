package com.example.serveez.repository;

import com.example.serveez.model.ServiceListing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceListingRepository extends MongoRepository<ServiceListing, String> {
    List<ServiceListing> findByProviderId(String providerId);

    List<ServiceListing> findByCategoryIdAndIsActiveTrue(String categoryId);

    List<ServiceListing> findByIsActiveTrue();
}
