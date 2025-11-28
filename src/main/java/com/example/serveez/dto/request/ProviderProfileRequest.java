package com.example.serveez.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProviderProfileRequest {
    @NotBlank(message = "Display name is required")
    private String displayName;

    private String bio;

    private LocationRequest location;

    private String phone;

    private String additionalContacts;

    @Data
    public static class LocationRequest {
        private String city;
        private String area;
        private Double latitude;
        private Double longitude;
    }
}
