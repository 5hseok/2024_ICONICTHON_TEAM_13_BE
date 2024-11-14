package com.prochord.server.domain.member;

import com.prochord.server.domain.profile.ProfessorInterest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "professor")
public class Professor extends Member {

    @Column(name = "department", nullable = false)
    private String department;

}