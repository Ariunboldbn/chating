package com.example.demo.config;

import com.example.demo.model.ChatRoom;
import com.example.demo.model.Message;
import com.example.demo.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ChatHandler extends TextWebSocketHandler {

    final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChatRoomService chatRoomService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String uri = session.getUri().toString();
        Optional.ofNullable(UriComponentsBuilder.fromUriString(uri).build().getQueryParams().getFirst("userId"))
                .ifPresentOrElse(
                        userId -> {
                            sessions.put(userId, session);
                            log.info("WebSocket session established for userId: {}", userId);
                        },
                        () -> log.warn("User ID is null", session.getId()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Message msg = objectMapper.readValue(payload, Message.class);

        log.info("Received message {}", msg.getContent());

        List<String> participants = getChatRoomParticipants(msg.getChatRoomId());

        for (String participantId : participants) {
            WebSocketSession recipientSession = sessions.get(participantId);
            if (recipientSession != null && recipientSession.isOpen()) {
                recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
                log.info("Message sent to user {}", participantId);
            } else {
                log.error("Recipient session not found {}", participantId);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String uri = session.getUri().toString();
        String userId = UriComponentsBuilder.fromUriString(uri).build().getQueryParams().getFirst("userId");

        if (userId != null) {
            sessions.remove(userId);
        }
    }

    private List<String> getChatRoomParticipants(String chatRoomId) {
        ChatRoom chatRoom = chatRoomService.findChatRoomById(chatRoomId);
        return chatRoom != null ? chatRoom.getParticipants() : Collections.emptyList();
    }
}
