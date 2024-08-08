package com.example.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "chatrooms")
public class ChatRoom extends BaseModel{
    private List<String> participants;
}
