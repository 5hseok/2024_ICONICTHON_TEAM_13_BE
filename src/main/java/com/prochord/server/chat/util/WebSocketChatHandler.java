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

import java.io.IOException;
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
        WebSocketMessage webSocketMessage = (WebSocketMessage) objectMapper.readValue(message.getPayload(), WebSocketMessage.class);
        switch (webSocketMessage.getType().getValue()) {
            case "ENTER" -> enterChatRoom(webSocketMessage.getPayload(), session);
            case "TALK" -> sendMessage(username, webSocketMessage.getPayload());
            case "EXIT" -> exitChatRoom(session, webSocketMessage.getPayload());
            case "JOIN" -> joinChatRoom(webSocketMessage.getPayload(), session);
        }
    }

    /**
     * 메시지 전송
     * @param chatDto ChatDto
     */
    private void sendMessage(String username, ChatDto chatDto) {
        log.info("send chatDto : {}", chatDto.toString());
        ChatRoom chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        log.info("chatRoomMap : {}", chatRoomMap.toString());
        chatRoom.sendMessage(username, chatDto);
    }

    /**
     * 채팅방 입장
     * @param chatDto ChatDto
     * @param session 웹소켓 세션
     */
    private void enterChatRoom(ChatDto chatDto, WebSocketSession session) {
        log.info("Entering chat room with chatDto: {}", chatDto.toString());
        log.info("Current chatRoomMap: {}", chatRoomMap.toString());

        ChatRoom chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        if (chatRoom == null) {
            log.info("Chat room with id {} not found. Creating a new chat room.", chatDto.getChatRoomId());
            chatRoom = new ChatRoom(objectMapper);
        }
        log.info("Chat room: {}", chatRoom.toString());
        chatDto.setMessage(chatDto.getUsername() + "님이 입장하셨습니다.");
        log.info("ChatDto: {}", chatDto.toString());
        chatRoom.enter(chatDto, session);

        chatRoomMap.put(chatDto.getChatRoomId(), chatRoom);
        log.info("Updated chatRoomMap: {}", chatRoomMap.toString());
    }

    /**
     * 채팅방 참여 (JOIN)
     * @param chatDto ChatDto
     * @param session 웹소켓 세션
     */
    private void joinChatRoom(ChatDto chatDto, WebSocketSession session) {
        log.info("Joining chat room with chatDto: {}", chatDto.toString());
        ChatRoom chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        if (chatRoom == null) {
            log.error("Chat room with id {} not found.", chatDto.getChatRoomId());
            return;
        }
        chatDto.setMessage(chatDto.getUsername() + "님이 참여하셨습니다.");
        chatRoom.join(chatDto, session);
        log.info("Updated chatRoomMap: {}", chatRoomMap.toString());
    }

    /**
     * 채팅방 퇴장
     * @param chatDto ChatDto
     */
    private void exitChatRoom(WebSocketSession session, ChatDto chatDto) {
        log.info("exit chatDto : {}", chatDto.toString());
        chatDto.setMessage(chatDto.getUsername() + "님이 퇴장하셨습니다.");
        ChatRoom chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        log.info("chatRoomMap : {}", chatRoomMap.toString());
        log.info("chatRoom : {}", chatRoom.toString());
        chatRoom.exit(chatDto);

        if(chatRoom.getActiveUserMap().isEmpty()){
            chatRoomMap.remove(chatDto.getChatRoomId());
        }

        try{
            session.close();
        } catch (Exception e){
            log.error("Failed to close session: {}", e.getMessage());
        }
    }
}