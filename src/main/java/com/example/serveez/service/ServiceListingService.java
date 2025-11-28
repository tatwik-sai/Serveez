package com.example.serveez.service;

import com.example.serveez.dto.request.ServiceListingRequest;
import com.example.serveez.dto.response.ServiceListingResponse;
import com.example.serveez.exception.BadRequestException;
import com.example.serveez.exception.ResourceNotFoundException;
import com.example.serveez.exception.UnauthorizedException;
import com.example.serveez.model.ServiceListing;
import com.example.serveez.repository.ProviderProfileRepository;
import com.example.serveez.repository.ServiceCategoryRepository;
import com.example.serveez.repository.ServiceListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceListingService {

    private final ServiceListingRepository listingRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final ProviderProfileRepository profileRepository;
    private final FileStorageService fileStorageService;

    public ServiceListingResponse createListing(String providerId, ServiceListingRequest request) {
        // Verify provider has a profile
        if (!profileRepository.existsByUserId(providerId)) {
            throw new BadRequestException("Provider profile must be created before creating listings");
        }

        // Verify category exists
        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new BadRequestException("Invalid category ID");
        }

        ServiceListing listing = new ServiceListing();
        listing.setProviderId(providerId);
        listing.setCategoryId(request.getCategoryId());
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setPrice(request.getPrice());
        listing.setLocation(request.getLocation());
        listing.setEstimatedDuration(request.getEstimatedDuration());
        listing.setIsActive(true);
        listing.setCreatedAt(LocalDateTime.now());
        listing.setUpdatedAt(LocalDateTime.now());

        listing = listingRepository.save(listing);
        return mapToResponse(listing);
    }

    public List<ServiceListingResponse> getProviderListings(String providerId) {
        return listingRepository.findByProviderId(providerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ServiceListingResponse getListingById(String id) {
        ServiceListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service listing not found"));
        return mapToResponse(listing);
    }

    public List<ServiceListingResponse> getAllActiveListings(String categoryId) {
        if (categoryId != null && !categoryId.isEmpty()) {
            return listingRepository.findByCategoryIdAndIsActiveTrue(categoryId).stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
        return listingRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ServiceListingResponse updateListing(String providerId, String id, ServiceListingRequest request) {
        ServiceListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service listing not found"));

        if (!listing.getProviderId().equals(providerId)) {
            throw new UnauthorizedException("You can only update your own listings");
        }

        listing.setCategoryId(request.getCategoryId());
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setPrice(request.getPrice());
        listing.setLocation(request.getLocation());
        listing.setEstimatedDuration(request.getEstimatedDuration());
        listing.setUpdatedAt(LocalDateTime.now());

        listing = listingRepository.save(listing);
        return mapToResponse(listing);
    }

    public void deleteListing(String providerId, String id) {
        ServiceListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service listing not found"));

        if (!listing.getProviderId().equals(providerId)) {
            throw new UnauthorizedException("You can only delete your own listings");
        }

        listing.setIsActive(false);
        listing.setUpdatedAt(LocalDateTime.now());
        listingRepository.save(listing);
    }

    public ServiceListingResponse uploadPhoto(String providerId, String listingId, MultipartFile file, String caption) {
        ServiceListing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Service listing not found"));

        if (!listing.getProviderId().equals(providerId)) {
            throw new UnauthorizedException("You can only upload photos to your own listings");
        }

        String fileName = fileStorageService.storeFile(file, "listings/" + listingId);

        ServiceListing.Photo photo = new ServiceListing.Photo();
        photo.setId(UUID.randomUUID().toString());
        photo.setUrl(fileName);
        photo.setCaption(caption);
        photo.setUploadedAt(LocalDateTime.now());

        listing.getPhotos().add(photo);
        listing.setUpdatedAt(LocalDateTime.now());

        listing = listingRepository.save(listing);
        return mapToResponse(listing);
    }

    public ServiceListingResponse deletePhoto(String providerId, String listingId, String photoId) {
        ServiceListing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Service listing not found"));

        if (!listing.getProviderId().equals(providerId)) {
            throw new UnauthorizedException("You can only delete photos from your own listings");
        }

        ServiceListing.Photo photoToRemove = listing.getPhotos().stream()
                .filter(p -> p.getId().equals(photoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));

        fileStorageService.deleteFile(photoToRemove.getUrl());
        listing.getPhotos().remove(photoToRemove);
        listing.setUpdatedAt(LocalDateTime.now());

        listing = listingRepository.save(listing);
        return mapToResponse(listing);
    }

    public void updateListingStatus(String id, boolean isActive) {
        ServiceListing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service listing not found"));

        listing.setIsActive(isActive);
        listing.setUpdatedAt(LocalDateTime.now());
        listingRepository.save(listing);
    }

    private ServiceListingResponse mapToResponse(ServiceListing listing) {
        return new ServiceListingResponse(
                listing.getId(),
                listing.getProviderId(),
                listing.getCategoryId(),
                listing.getTitle(),
                listing.getDescription(),
                listing.getPrice(),
                listing.getLocation(),
                listing.getEstimatedDuration(),
                listing.getPhotos(),
                listing.getIsActive(),
                listing.getCreatedAt());
    }
}
