package com.prochord.server.domain.profile;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_subscribe_professor")
public class StudentSubscribeProfessor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student", nullable = false)
    private Long student;

    @Column(name = "professor_id", nullable = false)
    private Long professorId;
}