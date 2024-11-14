package com.prochord.server.repository;

import com.prochord.server.domain.member.Member;
import com.prochord.server.global.exception.BusinessException;
import com.prochord.server.global.exception.message.ErrorMessage;
import com.prochord.server.global.jwt.JwtTokenProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface MemberRepository<T extends Member> extends JpaRepository<T, Long> {

    // 이메일로 사용자 조회
    Optional<T> findByEmail(String email);

    default T findByEmailOrElseThrow(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER));
    }

    default T findByToken(String token, JwtTokenProvider jwtTokenProvider) {
        return findById(jwtTokenProvider.getUserFromJwt(token))
                .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER)); // userId로 User 객체 조회
    }
}