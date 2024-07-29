package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.Model.ChatMessage;
import com.example.demo.Repository.ChatRepository;

@Controller
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        try {
            chatRepository.save(chatMessage);
        } catch (Exception e) {
            System.err.println("Error saving chat message: " + e.getMessage());
        }
        return chatMessage;
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        System.err.println("Exception in ChatController: " + e.getMessage());
    }
}
