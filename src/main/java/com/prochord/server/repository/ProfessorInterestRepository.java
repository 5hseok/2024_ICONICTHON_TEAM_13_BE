package com.prochord.server.repository;

import com.prochord.server.domain.member.Professor;
import com.prochord.server.domain.profile.ProfessorInterest;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfessorInterestRepository extends JpaRepository<ProfessorInterest, Long> {

    @Query("SELECT i FROM ProfessorInterest i JOIN FETCH i.professor WHERE i.professor.id = :professorId")
    List<ProfessorInterest> findAllByProfessorId(@Param("professorId") Long professorId);

    List<ProfessorInterest> findAllByProfessor(Professor professor);

}

