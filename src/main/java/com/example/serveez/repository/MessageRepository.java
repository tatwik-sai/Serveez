package com.example.serveez.repository;

import com.example.serveez.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByBookingIdOrderByCreatedAtAsc(String bookingId);

    @Query("{ '$or': [ { 'fromUserId': ?0 }, { 'toUserId': ?0 } ] }")
    List<Message> findConversationsByUserId(String userId);

    @Query("{ 'bookingId': ?0, '$or': [ { 'fromUserId': ?1, 'toUserId': ?2 }, { 'fromUserId': ?2, 'toUserId': ?1 } ] }")
    List<Message> findByBookingIdAndParticipants(String bookingId, String userId1, String userId2);
}
