package com.prochord.server.config;

import com.prochord.server.utils.WebSocketAuthInterceptor;
import com.prochord.server.utils.WebSocketChatHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import java.util.logging.Logger;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketChatHandler webSocketChatHandler;
    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(WebSocketConfig.class);


//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        // 메시지 브로커는 Redis로 설정됨
//        config.enableSimpleBroker("/topic", "/queue");
//        config.setApplicationDestinationPrefixes("/app");
//        logger.info("Message Broker configured with prefixes: /topic, /queue, and application destination prefix: /app");
//
//    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // WebSocket 연결할 엔드포인트 설정
        registry.addHandler(webSocketChatHandler, "/chats").addInterceptors(webSocketAuthInterceptor).setAllowedOrigins("*");
        logger.info("STOMP endpoint registered at /ws/chat with SockJS support and allowed origin patterns: *");

    }
}