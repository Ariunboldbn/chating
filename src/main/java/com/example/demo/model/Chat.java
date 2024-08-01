package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "chats")
public class Chat {
    @Id
    private String id;
    private String type; // "one-to-one" or "group"
    private List<String> participants; // List of user IDs
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
