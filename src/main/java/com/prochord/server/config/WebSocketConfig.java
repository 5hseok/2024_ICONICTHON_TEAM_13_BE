package com.prochord.server.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import java.util.logging.Logger;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(WebSocketConfig.class);


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커는 Redis로 설정됨
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        logger.info("Message Broker configured with prefixes: /topic, /queue, and application destination prefix: /app");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 연결할 엔드포인트 설정
        registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*").withSockJS();
        logger.info("STOMP endpoint registered at /ws/chat with SockJS support and allowed origin patterns: *");

    }
}