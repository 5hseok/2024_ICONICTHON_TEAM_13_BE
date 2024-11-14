package com.prochord.server.chat.domain;

/**
 * 웹소켓 메시지 타입
 */
public enum WebSocketMessageType {
    ENTER("ENTER"),
    JOIN("JOIN"),
    TALK("TALK"),
    EXIT("EXIT"),
    SUB("SUBSCRIBE"),
    PUB("PUBLISH"),
    NEW_ROOM("NEW_ROOM"),
    NEW_ID("NEW_ID");

    private final String type;

    WebSocketMessageType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.type;
    }
}