package com.example.serveez.controller;

import com.example.serveez.dto.request.ServiceListingRequest;
import com.example.serveez.dto.response.ApiResponse;
import com.example.serveez.dto.response.ServiceListingResponse;
import com.example.serveez.security.UserPrincipal;
import com.example.serveez.service.ServiceListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceListingController {

    private final ServiceListingService listingService;

    // Public endpoints
    @GetMapping("/api/listings")
    public ResponseEntity<List<ServiceListingResponse>> getAllListings(
            @RequestParam(required = false) String categoryId) {
        return ResponseEntity.ok(listingService.getAllActiveListings(categoryId));
    }

    @GetMapping("/api/listings/{id}")
    public ResponseEntity<ServiceListingResponse> getListingById(@PathVariable String id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    // Provider endpoints
    @PostMapping("/api/providers/listings")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ServiceListingResponse> createListing(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ServiceListingRequest request) {
        return ResponseEntity.ok(listingService.createListing(currentUser.getId(), request));
    }

    @GetMapping("/api/providers/listings")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<List<ServiceListingResponse>> getMyListings(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(listingService.getProviderListings(currentUser.getId()));
    }

    @PutMapping("/api/providers/listings/{id}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ServiceListingResponse> updateListing(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id,
            @Valid @RequestBody ServiceListingRequest request) {
        return ResponseEntity.ok(listingService.updateListing(currentUser.getId(), id, request));
    }

    @DeleteMapping("/api/providers/listings/{id}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse> deleteListing(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id) {
        listingService.deleteListing(currentUser.getId(), id);
        return ResponseEntity.ok(new ApiResponse(true, "Listing deleted successfully"));
    }

    @PostMapping("/api/providers/listings/{id}/photos")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ServiceListingResponse> uploadPhoto(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id,
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String caption) {
        return ResponseEntity.ok(listingService.uploadPhoto(currentUser.getId(), id, file, caption));
    }

    @DeleteMapping("/api/providers/listings/{id}/photos/{photoId}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ServiceListingResponse> deletePhoto(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id,
            @PathVariable String photoId) {
        return ResponseEntity.ok(listingService.deletePhoto(currentUser.getId(), id, photoId));
    }

    // Admin endpoints
    @GetMapping("/api/admin/listings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ServiceListingResponse>> getAllListingsAdmin(
            @RequestParam(required = false) String categoryId) {
        return ResponseEntity.ok(listingService.getAllActiveListings(categoryId));
    }

    @PatchMapping("/api/admin/listings/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateListingStatus(
            @PathVariable String id,
            @RequestParam boolean isActive) {
        listingService.updateListingStatus(id, isActive);
        return ResponseEntity.ok(new ApiResponse(true, "Listing status updated"));
    }
}
