package com.prochord.server.config;

//import com.prochord.server.utils.WebSocketAuthInterceptor;
import com.prochord.server.service.WebSocketChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketChatHandler webSocketChatHandler;
//    private final WebSocketAuthInterceptor webSocketAuthInterceptor;


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
        registry.addHandler(webSocketChatHandler, "/chats")
//                .addInterceptors(webSocketAuthInterceptor)
                .setAllowedOrigins("*");
    }
}