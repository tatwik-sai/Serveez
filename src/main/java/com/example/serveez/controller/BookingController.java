package com.example.serveez.controller;

import com.example.serveez.dto.request.BookingRequest;
import com.example.serveez.dto.response.BookingResponse;
import com.example.serveez.security.UserPrincipal;
import com.example.serveez.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // User endpoints
    @PostMapping("/api/bookings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookingResponse> createBooking(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(currentUser.getId(), request));
    }

    @GetMapping("/api/bookings/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(bookingService.getUserBookings(currentUser.getId()));
    }

    @GetMapping("/api/bookings/{id}")
    @PreAuthorize("hasAnyRole('USER', 'PROVIDER', 'ADMIN')")
    public ResponseEntity<BookingResponse> getBookingById(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id) {
        return ResponseEntity.ok(bookingService.getBookingById(id, currentUser.getId(), currentUser.getRole()));
    }

    @PostMapping("/api/bookings/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookingResponse> cancelBooking(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, currentUser.getId(), reason));
    }

    // Provider endpoints
    @GetMapping("/api/provider/bookings")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<List<BookingResponse>> getProviderBookings(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(bookingService.getProviderBookings(currentUser.getId()));
    }

    @GetMapping("/api/provider/bookings/{id}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<BookingResponse> getProviderBookingById(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id) {
        return ResponseEntity.ok(bookingService.getBookingById(id, currentUser.getId(), currentUser.getRole()));
    }

    @PostMapping("/api/bookings/{id}/confirm")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<BookingResponse> confirmBooking(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id, currentUser.getId()));
    }

    @PostMapping("/api/bookings/{id}/complete")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<BookingResponse> completeBooking(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable String id) {
        return ResponseEntity.ok(bookingService.completeBooking(id, currentUser.getId()));
    }

    // Admin endpoints
    @GetMapping("/api/admin/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getAllBookings(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(bookingService.getAllBookings(status));
    }

    @PostMapping("/api/admin/bookings/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingResponse> adminCancelBooking(
            @PathVariable String id,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(bookingService.adminCancelBooking(id, reason));
    }
}
