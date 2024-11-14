package com.prochord.server.dto.professor.response;

import com.prochord.server.domain.member.Professor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class ProfessorResponse {
    private Long id;
    private String name;
    private String department;
    private List<ProfessorInterestResponse> interests;

    public static ProfessorResponse of(Professor professor, List<ProfessorInterestResponse> interests) {
        return new ProfessorResponse(professor.getId(), professor.getName(), professor.getDepartment(), interests);
    }

    // Constructor
    public ProfessorResponse(Long id, String name, String department, List<ProfessorInterestResponse> interests) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.interests = interests;
    }
}