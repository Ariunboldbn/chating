package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public Message sendMessage(@RequestBody Message message, @RequestParam String creatorUserId) {
        return messageService.saveMessage(message, creatorUserId);
    }

    @GetMapping("/room/{chatRoomId}")
    public List<Message> getMessagesByChatRoomId(@PathVariable String chatRoomId) {
        return messageService.getMessagesByChatRoomId(chatRoomId);
    }
}
