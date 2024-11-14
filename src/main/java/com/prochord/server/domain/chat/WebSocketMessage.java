package com.prochord.server.domain.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prochord.server.dto.chat.ChatDto;
import lombok.Getter;

/**
 * 웹소켓 메시지 프로토콜
 */
@Getter
public class WebSocketMessage{
    private final WebSocketMessageType type;
    private final ChatDto payload;

    @JsonCreator
    public WebSocketMessage(
            @JsonProperty("type") WebSocketMessageType type,
            @JsonProperty("payload") ChatDto payload) {
        this.type = type;
        this.payload = payload;
    }
}