

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
        String username = chatDto.getUsername();

        // username 또는 session이 null이면 로그를 찍고 메소드 종료
        if (username == null || session == null) {
            log.error("Cannot enter chat room. Username or session is null. Username: {}, Session: {}", username, session);
            return;
        }

        activeUserMap.put(username, session);  // Null 체크 후 put 실행

        // 채팅방에 이미 있는 사용자들에게 입장 알림 보내기
        for (Map.Entry<String, WebSocketSession> entry : activeUserMap.entrySet()) {
            try {
                WebSocketSession userSession = entry.getValue();
                if (userSession.isOpen()) {  // 세션이 열려 있는지 확인
                    userSession.sendMessage(getTextMessage(WebSocketMessageType.ENTER, chatDto));
                } else {
                    log.warn("Session for user {} is closed. Removing from activeUserMap.", entry.getKey());
                    activeUserMap.remove(entry.getKey());
                }
            } catch (Exception e) {
                log.error("Failed to send enter message to {}: {}", entry.getKey(), e.getMessage());
            }
        }
    }

    /**
     * 채팅방 참여 (JOIN)
     * @param chatDto ChatDto
     * @param session 웹소켓 세션
     */
    public void join(ChatDto chatDto, WebSocketSession session) {
        String username = chatDto.getUsername();

        // username 또는 session이 null이면 로그를 찍고 메소드 종료
        if (username == null || session == null) {
            log.error("Cannot join chat room. Username or session is null. Username: {}, Session: {}", username, session);
            return;
        }

        // 사용자 맵에 사용자 추가
        activeUserMap.put(username, session);

        // 채팅방에 이미 있는 사용자들에게 참가 알림 보내기
        for (Map.Entry<String, WebSocketSession> entry : activeUserMap.entrySet()) {
            try {
                WebSocketSession userSession = entry.getValue();
                if (userSession.isOpen()) {  // 세션이 열려 있는지 확인
                    userSession.sendMessage(getTextMessage(WebSocketMessageType.JOIN, chatDto));
                } else {
                    log.warn("Session for user {} is closed. Removing from activeUserMap.", entry.getKey());
                    activeUserMap.remove(entry.getKey());
                }
            } catch (Exception e) {
                log.error("Failed to send join message to {}: {}", entry.getKey(), e.getMessage());
            }
        }
    }


    /**
     * 채팅방 퇴장
     * @param chatDto ChatDto
     */
    public void exit(ChatDto chatDto) {
        String username = chatDto.getUsername();
        activeUserMap.remove(username);
        log.info("User {} has left the chat room.", username);
        sendToAllExceptSender(WebSocketMessageType.EXIT, chatDto, username);
        log.info("Current active users: {}", activeUserMap.keySet());
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