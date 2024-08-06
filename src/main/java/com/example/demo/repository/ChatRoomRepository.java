package com.example.demo.repository;

import com.example.demo.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    @Query("{ 'participants': { $all: ?0, $size: 2 } }")
    Optional<ChatRoom> findByParticipants(List<String> participants);
}
