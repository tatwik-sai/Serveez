package com.example.serveez.controller;

import com.example.serveez.dto.request.MessageRequest;
import com.example.serveez.dto.response.MessageResponse;
import com.example.serveez.security.UserPrincipal;
import com.example.serveez.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // Authenticated user endpoints
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'PROVIDER')")
    public ResponseEntity<MessageResponse> sendMessage(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(currentUser.getId(), request));
    }

    @GetMapping("/thread")
    @PreAuthorize("hasAnyRole('USER', 'PROVIDER')")
    public ResponseEntity<List<MessageResponse>> getBookingMessages(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam String bookingId) {
        return ResponseEntity.ok(messageService.getBookingMessages(bookingId, currentUser.getId()));
    }

    @GetMapping("/conversations")
    @PreAuthorize("hasAnyRole('USER', 'PROVIDER')")
    public ResponseEntity<List<MessageResponse>> getUserConversations(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(messageService.getUserConversations(currentUser.getId()));
    }

    // Admin endpoints
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MessageResponse>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }
}
