package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.Model.ChatMessage;

public interface ChatRepository extends MongoRepository<ChatMessage, String> {
}
