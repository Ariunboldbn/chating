package com.example.demo.service;

import com.example.demo.model.ChatRoom;
import com.example.demo.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(String user1Id, String user2Id, String creatorUserId) {
        List<String> participants = Arrays.asList(user1Id, user2Id);
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findByParticipants(participants);

        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get();
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setParticipants(participants);
        chatRoom.setCreatedUserId(creatorUserId);
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setUpdatedAt(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom findChatRoomById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElse(null);
    }
}
