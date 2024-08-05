package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private String chatRoomId;
    private String senderId;
    private String content;
    private List<String> attachments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
