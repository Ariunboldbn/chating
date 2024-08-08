package com.example.demo.service;

import com.example.demo.model.Message;
import com.example.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message, String creatorUserId) {
        message.setCreatedUserId(creatorUserId);
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatRoomId(String chatRoomId) {
        return messageRepository.findByChatRoomId(chatRoomId);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(String id) {
        return messageRepository.findById(id).orElse(null);
    }

    public Message updateMessage(String id, Message message) {
        Message existingMessage = messageRepository.findById(id).orElse(null);
        if (existingMessage != null) {
            message.setId(id);
            message.setUpdatedAt(LocalDateTime.now());
            return messageRepository.save(message);
        }
        return null;
    }

    public void deleteMessage(String id) {
        messageRepository.deleteById(id);
    }
}
