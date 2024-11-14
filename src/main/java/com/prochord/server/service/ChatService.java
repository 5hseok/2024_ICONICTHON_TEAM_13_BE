package com.prochord.server.service;

import com.prochord.server.domain.chat.ChatMessage;
import com.prochord.server.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public void handleChatMessage(ChatMessage chatMessage) {
        // Redis를 통해 수신된 메시지를 MySQL에 저장
        chatMessage.updateDate(new Date()); // 메시지가 수신된 시간 설정
        chatMessageRepository.save(chatMessage);

        // WebSocket을 통해 클라이언트에 메시지 전달
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getChatRoom(), chatMessage);
    }
}