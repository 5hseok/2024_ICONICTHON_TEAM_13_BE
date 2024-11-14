package com.prochord.server.chat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prochord.server.chat.domain.WebSocketMessage;
import com.prochord.server.chat.dto.chat.ChatDto;
import com.prochord.server.chat.service.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final Map<Long, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        String username = (String) session.getAttributes().get("username");
        WebSocketMessage webSocketMessage = objectMapper.readValue(message.getPayload(), WebSocketMessage.class);

        switch (webSocketMessage.getType().getValue()) {
            case "ENTER" -> enterChatRoom(webSocketMessage.getPayload(), session);
            case "TALK" -> {
                ChatDto chatDto = webSocketMessage.getPayload();
                ChatRoom chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
                if (chatRoom == null) {
                    log.error("Chat room with ID {} does not exist. Message cannot be delivered.", chatDto.getChatRoomId());
                    try {
                        session.sendMessage(new TextMessage("Error: Chat room does not exist."));
                    } catch (Exception e) {
                        log.error("Failed to send error message to user {}: {}", username, e.getMessage());
                    }
                } else {
                    sendMessage(username, chatDto);
                }
            }
            case "EXIT" -> {
                ChatDto chatDto = webSocketMessage.getPayload();
                ChatRoom chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
                if (chatRoom != null) {
                    exitChatRoom(username, chatDto);
                } else {
                    log.warn("User {} attempted to exit a non-existent chat room with ID {}", username, chatDto.getChatRoomId());
                }
            }
            default -> log.warn("Unsupported message type received: {}", webSocketMessage.getType().getValue());
        }
    }

    /**
     * 메시지 전송
     * @param chatDto ChatDto
     */
    private void sendMessage(String username, ChatDto chatDto) {
        log.info("send chatDto : " + chatDto.toString());
        ChatRoom chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        chatRoom.sendMessage(username, chatDto);
    }

    /**
     * 채팅방 입장
     * @param chatDto ChatDto
     * @param session 웹소켓 세션
     */
    private void enterChatRoom(ChatDto chatDto, WebSocketSession session) {
        log.info("enter chatDto : " + chatDto.toString());
        chatDto.setMessage(chatDto.getUsername() + "님이 입장하셨습니다.");
        ChatRoom chatRoom = chatRoomMap.getOrDefault(chatDto.getChatRoomId(), new ChatRoom(objectMapper));
        chatRoom.enter(chatDto, session);
        chatRoomMap.put(chatDto.getChatRoomId(), chatRoom);
        log.info("Chat room {} added to chatRoomMap.", chatDto.getChatRoomId());
    }

    /**
     * 채팅방 퇴장
     * @param chatDto ChatDto
     */
    private void exitChatRoom(String username, ChatDto chatDto) {
        log.info("exit chatDto : " + chatDto.toString());
        chatDto.setMessage(chatDto.getUsername() + "님이 퇴장하셨습니다.");
        ChatRoom chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        chatRoom.exit(username, chatDto);

        if(chatRoom.getActiveUserMap().isEmpty()){
            chatRoomMap.remove(chatDto.getChatRoomId());
        }
    }
}

