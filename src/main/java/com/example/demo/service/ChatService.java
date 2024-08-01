package com.example.demo.service;

import com.example.demo.model.Chat;
import com.example.demo.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public Chat getChatById(String id) {
        return chatRepository.findById(id).orElse(null);
    }

    public Chat createChat(Chat chat) {
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }

    public Chat updateChat(String id, Chat chat) {
        Chat existingChat = chatRepository.findById(id).orElse(null);
        if (existingChat != null) {
            chat.setId(id);
            chat.setUpdatedAt(LocalDateTime.now());
            return chatRepository.save(chat);
        }
        return null;
    }

    public void deleteChat(String id) {
        chatRepository.deleteById(id);
    }
}
