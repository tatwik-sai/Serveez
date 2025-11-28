package com.example.serveez.controller;

import com.example.serveez.dto.request.ProviderProfileRequest;
import com.example.serveez.dto.response.ProviderProfileResponse;
import com.example.serveez.security.UserPrincipal;
import com.example.serveez.service.ProviderProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderProfileController {

    private final ProviderProfileService profileService;

    // Provider endpoints
    @PostMapping("/profile")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ProviderProfileResponse> createProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ProviderProfileRequest request) {
        return ResponseEntity.ok(profileService.createProfile(currentUser.getId(), request));
    }

    @GetMapping("/profile/me")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ProviderProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(profileService.getProfileByUserId(currentUser.getId()));
    }

    @PutMapping("/profile/me")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ProviderProfileResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ProviderProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(currentUser.getId(), request));
    }

    // Public endpoint
    @GetMapping("/{providerId}/profile")
    public ResponseEntity<ProviderProfileResponse> getProviderProfile(@PathVariable String providerId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(providerId));
    }
}
