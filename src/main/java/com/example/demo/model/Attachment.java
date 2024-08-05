package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "attachments")
public class Attachment {
    @Id
    private String id;
    private String messageId;
    private String type;
    private String url;
    private String filename;
    private long filesize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
