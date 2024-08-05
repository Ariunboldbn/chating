package com.example.demo.service;

import com.example.demo.model.ChatRoom;
import com.example.demo.repository.ChatRoomRepository;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(String user1Id, String user2Id) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setParticipants(Arrays.asList(user1Id, user2Id));
        return chatRoomRepository.save(chatRoom);
    }
}
