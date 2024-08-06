package com.example.demo.config;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String uri = session.getUri().toString();
        String userId = UriComponentsBuilder.fromUriString(uri).build().getQueryParams().getFirst("userId");
        
        if (userId != null) {
            sessions.put(userId, session);
            System.out.println("Connection established: User ID = " + userId);
        } else {
            System.out.println("User ID is null during connection establishment.");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        String[] parts = payload.split(":");
        if (parts.length < 2) {
            System.out.println("Invalid message format.");
            return;
        }
        String toUserId = parts[0].trim();
        String content = parts[1].trim();

        WebSocketSession recipientSession = sessions.get(toUserId);
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(content));
            System.out.println("Sent message to user " + toUserId);
        } else {
            System.out.println("Recipient session not found or closed for user ID: " + toUserId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String uri = session.getUri().toString();
        String userId = UriComponentsBuilder.fromUriString(uri).build().getQueryParams().getFirst("userId");
        
        if (userId != null) {
            sessions.remove(userId);
            System.out.println("Connection closed: User ID = " + userId);
        }
    }
}
