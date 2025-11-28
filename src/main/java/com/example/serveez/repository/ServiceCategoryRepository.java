package com.example.serveez.repository;

import com.example.serveez.model.ServiceCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceCategoryRepository extends MongoRepository<ServiceCategory, String> {
    Optional<ServiceCategory> findByName(String name);

    List<ServiceCategory> findByIsActiveTrue();
}
