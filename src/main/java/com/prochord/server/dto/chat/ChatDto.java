package com.prochord.server.dto.chat;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatDto {
    private String content;
    private final Long chatRoomId;
    private final String username;

    @JsonCreator
    public ChatDto(@JsonProperty("chatRoomId") Long chatRoomId,
                   @JsonProperty("username") String username,
                   @JsonProperty("content") String content) {
        this.chatRoomId = chatRoomId;
        this.username = username;
        this.content = content;
    }

    public void setMessage(String content) {
        this.content = content;
    }
}
