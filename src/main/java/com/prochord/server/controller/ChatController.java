//package com.prochord.server.controller;
//
//import com.prochord.server.domain.chat.ChatMessage;
////import com.prochord.server.service.ChatService;
//import jakarta.persistence.Column;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequiredArgsConstructor
//public class ChatController {
//
////    private final ChatService chatService;
//
//    @MessageMapping("/chat/send")
//    @SendTo("/topic/chat")
//    public void sendMessage(ChatMessage chatMessage) {
//        // 클라이언트로부터 메시지를 수신했을 때 Redis로 전달
//        chatService.handleChatMessage(chatMessage);
//    }
//}