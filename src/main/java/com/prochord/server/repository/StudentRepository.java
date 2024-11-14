package com.prochord.server.repository;

import com.prochord.server.domain.member.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MemberRepository<Student> {
}
