package com.prochord.server.chat.config;

//import com.prochord.server.utils.WebSocketAuthInterceptor;
import com.prochord.server.chat.util.WebSocketChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketChatHandler webSocketChatHandler;


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