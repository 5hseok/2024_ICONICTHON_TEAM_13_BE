package com.prochord.server.repository;

import com.prochord.server.domain.member.Professor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends MemberRepository<Professor> {
}