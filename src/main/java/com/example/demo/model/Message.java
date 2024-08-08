package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "messages")
public class Message extends BaseModel{
    private String chatRoomId;
    private String senderId;
    private String content;
    private List<String> attachments;
}
