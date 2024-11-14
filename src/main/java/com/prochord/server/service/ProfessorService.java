package com.prochord.server.service;


import com.prochord.server.domain.member.Professor;
import com.prochord.server.domain.profile.ProfessorInterest;
import com.prochord.server.dto.professor.response.ProfessorInterestResponse;
import com.prochord.server.dto.professor.response.ProfessorResponse;
import com.prochord.server.repository.ProfessorInterestRepository;
import com.prochord.server.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorInterestRepository professorInterestRepository;
    private final ProfessorRepository professorRepository;

    public ProfessorResponse getProfessorWithInterests(Long professorId) {
        List<ProfessorInterest> interests = professorInterestRepository.findAllByProfessorId(professorId);

        if (interests.isEmpty()) {
            throw new IllegalArgumentException("Professor not found or has no interests");
        }

        // 첫 번째 관심사에서 교수 정보를 가져옴 (모든 관심사에 같은 교수 정보가 있기 때문에)
        Professor professor = interests.getFirst().getProfessor();

        // 관심사 정보를 DTO로 변환
        List<ProfessorInterestResponse> interestResponses = interests.stream()
                .map(ProfessorInterestResponse::of)
                .collect(Collectors.toList());

        return new ProfessorResponse(professor.getId(), professor.getName(), professor.getDepartment(), interestResponses);
    }

    public List<ProfessorResponse> getAllProfessorsWithInterests() {
        // 모든 Professor를 가져옴
        List<Professor> professors = professorRepository.findAll();

        if (professors.isEmpty()) {
            throw new IllegalArgumentException("No professors found");
        }

        // 각 Professor에 대해 그와 연결된 관심사들을 찾아 ProfessorResponse 리스트로 만듦
        return professors.stream()
                .map(professor -> {
                    // 각 Professor에 대한 관심사를 ProfessorInterestRepository를 통해 가져옴
                    List<ProfessorInterest> interests = professorInterestRepository.findAllByProfessor(professor);

                    List<ProfessorInterestResponse> interestResponses = interests.stream()
                            .map(ProfessorInterestResponse::of)
                            .collect(Collectors.toList());

                    return new ProfessorResponse(professor.getId(), professor.getName(), professor.getDepartment(), interestResponses);
                })
                .collect(Collectors.toList());
    }

}
