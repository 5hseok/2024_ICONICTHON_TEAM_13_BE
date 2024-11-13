package com.prochord.server.domain.member;

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
@Table(name = "professor")
public class Professor extends Member {
    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "professor_image")
    private String professorImage;
}