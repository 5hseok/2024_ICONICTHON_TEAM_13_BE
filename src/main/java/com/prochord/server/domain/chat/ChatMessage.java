package com.prochord.server.domain.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_room", nullable = false)
    private Long chatRoom;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "professor_id", nullable = false)
    private Long professorId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "sender_type", nullable = false)
    private String senderType;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "sent", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date sent;

    public void updateDate(Date sent){
        this.sent = sent;
    }
}