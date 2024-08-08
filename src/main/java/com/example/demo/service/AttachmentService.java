package com.example.demo.service;

import com.example.demo.model.Attachment;
import com.example.demo.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAll();
    }

    public Attachment getAttachmentById(String id) {
        return attachmentRepository.findById(id).orElse(null);
    }

    public Attachment createAttachment(Attachment attachment, String creatorUserId) {
        attachment.setCreatedAt(LocalDateTime.now());
        attachment.setUpdatedAt(LocalDateTime.now());
        attachment.setCreatedUserId(creatorUserId);
        return attachmentRepository.save(attachment);
    }

    public Attachment updateAttachment(String id, Attachment attachment) {
        Attachment existingAttachment = attachmentRepository.findById(id).orElse(null);
        if (existingAttachment != null) {
            attachment.setId(id);
            attachment.setUpdatedAt(LocalDateTime.now());
            return attachmentRepository.save(attachment);
        }
        return null;
    }

    public void deleteAttachment(String id) {
        attachmentRepository.deleteById(id);
    }
}