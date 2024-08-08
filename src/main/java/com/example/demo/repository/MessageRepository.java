package com.example.demo.repository;

import com.example.demo.model.Message;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    @Query("{ 'chatRoomId': ?0, 'voided': false }")
    List<Message> findByChatRoomId(String chatRoomId);
}