package com.example.demo.controller;

import com.example.demo.model.ChatRoom;
import com.example.demo.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping
    public ChatRoom createChatRoom(@RequestParam String user1Id, @RequestParam String user2Id) {
        return chatRoomService.createChatRoom(user1Id, user2Id);
    }
}
