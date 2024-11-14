package com.prochord.server.domain.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "professor_id", nullable = false)
    private Long professorId;

    public static ChatRoom create(Long studentId, Long professorId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.studentId = studentId;
        chatRoom.professorId = professorId;
        return chatRoom;
    }
}
