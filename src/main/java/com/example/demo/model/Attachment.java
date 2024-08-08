package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.mongodb.core.mapping.Document;


@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "attachments")
public class Attachment extends BaseModel {
    private String messageId;
    private String type;
    private String url;
    private String filename;
    private long filesize;
}
