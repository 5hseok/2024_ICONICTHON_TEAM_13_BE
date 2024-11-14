package com.prochord.server.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prochord.server.chat.domain.WebSocketMessage;
import com.prochord.server.chat.domain.WebSocketMessageType;
import com.prochord.server.chat.dto.chat.ChatDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Getter
@RequiredArgsConstructor
public class ChatRoom {
    private final Map<String, WebSocketSession> activeUserMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    /**
     * 채팅방 입장
     * @param chatDto ChatDto
     * @param session 웹소켓 세션
     */
    public void enter(ChatDto chatDto, WebSocketSession session) {
        String username = (String) session.getAttributes().get("username");
        activeUserMap.put(username, session);
        sendToAllExceptSender(WebSocketMessageType.ENTER, chatDto, username);
    }

    /**
     * 채팅방 퇴장
     * @param username 사용자 이름
     * @param chatDto ChatDto
     */
    public void exit(String username, ChatDto chatDto) {
        activeUserMap.remove(username);
        sendToAllExceptSender(WebSocketMessageType.EXIT, chatDto, username);
    }

    /**
     * 메시지 전송
     * @param username 사용자 이름
     * @param chatDto ChatDto
     */
    public void sendMessage(String username, ChatDto chatDto) {
        sendToAllExceptSender(WebSocketMessageType.TALK, chatDto, username);
    }

    /**
     * 메시지를 전송합니다.
     * 특정 사용자에게 메시지를 전송하지 않습니다.
     * @param type 메시지 타입
     * @param chatDto ChatDto
     * @param senderUsername 메시지를 보낸 사용자 이름
     */
    private void sendToAllExceptSender(WebSocketMessageType type, ChatDto chatDto, String senderUsername) {
        for (Map.Entry<String, WebSocketSession> entry : activeUserMap.entrySet()) {
            try {
                if (!entry.getKey().equals(senderUsername)) {
                    entry.getValue().sendMessage(getTextMessage(type, chatDto));
                }
            } catch (Exception e) {
                log.error("Failed to send message to user {}: {}", entry.getKey(), e.getMessage());
            }
        }
    }

    /**
     * 메시지 전송
     * @param type 메시지 타입
     * @param chatDto ChatDto
     * @return TextMessage
     */
    private TextMessage getTextMessage(WebSocketMessageType type, ChatDto chatDto) {
        try {
            return new TextMessage(
                    objectMapper.writeValueAsString(
                            new WebSocketMessage(type, chatDto)
                    ));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message: {}", e.getMessage());
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}
