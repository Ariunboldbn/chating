package com.example.demo.controller;

import com.example.demo.model.Attachment;
import com.example.demo.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    @GetMapping
    public List<Attachment> getAllAttachments() {
        return attachmentService.getAllAttachments();
    }

    @GetMapping("/{id}")
    public Attachment getAttachmentById(@PathVariable String id) {
        return attachmentService.getAttachmentById(id);
    }

    @PostMapping
    public Attachment createAttachment(@RequestBody Attachment attachment, @RequestParam String creatorUserId) {
        return attachmentService.createAttachment(attachment, creatorUserId);
    }

    @PutMapping("/{id}")
    public Attachment updateAttachment(@PathVariable String id, @RequestBody Attachment attachment) {
        return attachmentService.updateAttachment(id, attachment);
    }

    @DeleteMapping("/{id}")
    public void deleteAttachment(@PathVariable String id) {
        attachmentService.deleteAttachment(id);
    }
}
