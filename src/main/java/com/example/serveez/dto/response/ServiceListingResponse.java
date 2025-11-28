package com.example.serveez.dto.response;

import com.example.serveez.model.ServiceListing;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ServiceListingResponse {
    private String id;
    private String providerId;
    private String categoryId;
    private String title;
    private String description;
    private Double price;
    private String location;
    private Integer estimatedDuration;
    private List<ServiceListing.Photo> photos;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
