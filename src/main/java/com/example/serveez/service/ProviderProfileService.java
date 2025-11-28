package com.example.serveez.service;

import com.example.serveez.dto.request.ProviderProfileRequest;
import com.example.serveez.dto.response.ProviderProfileResponse;
import com.example.serveez.exception.BadRequestException;
import com.example.serveez.exception.ResourceNotFoundException;
import com.example.serveez.model.ProviderProfile;
import com.example.serveez.repository.ProviderProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProviderProfileService {

    private final ProviderProfileRepository profileRepository;

    public ProviderProfileResponse createProfile(String userId, ProviderProfileRequest request) {
        if (profileRepository.existsByUserId(userId)) {
            throw new BadRequestException("Provider profile already exists");
        }

        ProviderProfile profile = new ProviderProfile();
        profile.setUserId(userId);
        profile.setDisplayName(request.getDisplayName());
        profile.setBio(request.getBio());
        profile.setPhone(request.getPhone());
        profile.setAdditionalContacts(request.getAdditionalContacts());

        if (request.getLocation() != null) {
            ProviderProfile.Location location = new ProviderProfile.Location();
            location.setCity(request.getLocation().getCity());
            location.setArea(request.getLocation().getArea());
            location.setLatitude(request.getLocation().getLatitude());
            location.setLongitude(request.getLocation().getLongitude());
            profile.setLocation(location);
        }

        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());

        profile = profileRepository.save(profile);
        return mapToResponse(profile);
    }

    public ProviderProfileResponse getProfileByUserId(String userId) {
        ProviderProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider profile not found"));
        return mapToResponse(profile);
    }

    public ProviderProfileResponse getProfileById(String id) {
        ProviderProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider profile not found"));
        return mapToResponse(profile);
    }

    public ProviderProfileResponse updateProfile(String userId, ProviderProfileRequest request) {
        ProviderProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider profile not found"));

        profile.setDisplayName(request.getDisplayName());
        profile.setBio(request.getBio());
        profile.setPhone(request.getPhone());
        profile.setAdditionalContacts(request.getAdditionalContacts());

        if (request.getLocation() != null) {
            ProviderProfile.Location location = new ProviderProfile.Location();
            location.setCity(request.getLocation().getCity());
            location.setArea(request.getLocation().getArea());
            location.setLatitude(request.getLocation().getLatitude());
            location.setLongitude(request.getLocation().getLongitude());
            profile.setLocation(location);
        }

        profile.setUpdatedAt(LocalDateTime.now());

        profile = profileRepository.save(profile);
        return mapToResponse(profile);
    }

    public void updateRating(String providerId, double averageRating, int totalReviews) {
        ProviderProfile profile = profileRepository.findByUserId(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider profile not found"));

        profile.setAverageRating(averageRating);
        profile.setTotalReviews(totalReviews);
        profile.setUpdatedAt(LocalDateTime.now());

        profileRepository.save(profile);
    }

    private ProviderProfileResponse mapToResponse(ProviderProfile profile) {
        return new ProviderProfileResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getDisplayName(),
                profile.getBio(),
                profile.getLocation(),
                profile.getPhone(),
                profile.getAdditionalContacts(),
                profile.getAverageRating(),
                profile.getTotalReviews(),
                profile.getCreatedAt());
    }
}
