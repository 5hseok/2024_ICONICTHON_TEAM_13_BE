package com.prochord.server.dto.professor.response;

import com.prochord.server.domain.profile.ProfessorInterest;
import lombok.Getter;

@Getter
public class ProfessorInterestResponse {
    private Long id;
    private String interest;

    public static ProfessorInterestResponse of(ProfessorInterest interest) {
        return new ProfessorInterestResponse(interest.getId(), interest.getInterest());
    }

    // Constructor
    public ProfessorInterestResponse(Long id, String interest) {
        this.id = id;
        this.interest = interest;
    }
}