package com.example.serveez.service;

import com.example.serveez.dto.request.MessageRequest;
import com.example.serveez.dto.response.MessageResponse;
import com.example.serveez.exception.BadRequestException;
import com.example.serveez.exception.ResourceNotFoundException;
import com.example.serveez.exception.UnauthorizedException;
import com.example.serveez.model.Booking;
import com.example.serveez.model.Message;
import com.example.serveez.repository.BookingRepository;
import com.example.serveez.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    public MessageResponse sendMessage(String fromUserId, MessageRequest request) {
        // Validate booking if provided
        if (request.getBookingId() != null) {
            Booking booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

            // Verify sender is part of the booking
            if (!booking.getUserId().equals(fromUserId) && !booking.getProviderId().equals(fromUserId)) {
                throw new UnauthorizedException("You can only send messages for your own bookings");
            }

            // Verify receiver is the other party in the booking
            if (!request.getToUserId().equals(booking.getUserId()) &&
                    !request.getToUserId().equals(booking.getProviderId())) {
                throw new BadRequestException("Recipient must be part of the booking");
            }
        }

        Message message = new Message();
        message.setFromUserId(fromUserId);
        message.setToUserId(request.getToUserId());
        message.setBookingId(request.getBookingId());
        message.setServiceListingId(request.getServiceListingId());
        message.setContent(request.getContent());
        message.setCreatedAt(LocalDateTime.now());

        message = messageRepository.save(message);

        // Notify recipient about new message
        notificationService.createNotification(
                request.getToUserId(),
                com.example.serveez.model.Notification.NotificationType.MESSAGE_RECEIVED,
                "New Message",
                "You have received a new message",
                request.getBookingId(),
                request.getServiceListingId(),
                fromUserId,
                null);

        return mapToResponse(message);
    }

    public List<MessageResponse> getBookingMessages(String bookingId, String currentUserId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Verify user is part of the booking
        if (!booking.getUserId().equals(currentUserId) && !booking.getProviderId().equals(currentUserId)) {
            throw new UnauthorizedException("You can only view messages for your own bookings");
        }

        return messageRepository.findByBookingIdOrderByCreatedAtAsc(bookingId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getUserConversations(String userId) {
        return messageRepository.findConversationsByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse mapToResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getFromUserId(),
                message.getToUserId(),
                message.getBookingId(),
                message.getServiceListingId(),
                message.getContent(),
                message.getCreatedAt(),
                message.getReadAt());
    }
}
