package com.example.serveez.repository;

import com.example.serveez.model.ProviderProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderProfileRepository extends MongoRepository<ProviderProfile, String> {
    Optional<ProviderProfile> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
