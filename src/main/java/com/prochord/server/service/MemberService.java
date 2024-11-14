package com.prochord.server.service;

import com.prochord.server.domain.member.Member;
import com.prochord.server.domain.member.Professor;
import com.prochord.server.domain.member.Student;
import com.prochord.server.dto.member.request.MemberCreateRequest;
import com.prochord.server.dto.member.request.MemberLoginRequest;
import com.prochord.server.dto.member.response.MemberLoginResponse;
import com.prochord.server.dto.member.response.MemberResponse;
import com.prochord.server.global.exception.BusinessException;
import com.prochord.server.global.exception.message.ErrorMessage;
import com.prochord.server.global.jwt.JwtTokenProvider;
import com.prochord.server.global.jwt.JwtValidationType;
import com.prochord.server.global.jwt.UserAuthentication;
import com.prochord.server.repository.ProfessorRepository;
import com.prochord.server.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j // 다양한 로깅 프레임워크(예: Logback, Log4j, JUL)를 사용할 수 있게하는 어노테이션
@Service
@RequiredArgsConstructor
public class MemberService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그아웃된 토큰을 저장하는 블랙리스트
    private final Set<String> tokenBlacklist = new HashSet<>();

    @Transactional
    public MemberResponse signUp(MemberCreateRequest memberCreateRequest) {
        // 이메일 중복 체크 (학생과 교수 모두 확인)
        if (studentRepository.findByEmail(memberCreateRequest.getEmail()).isPresent() ||
                professorRepository.findByEmail(memberCreateRequest.getEmail()).isPresent()) {
            throw new BusinessException(ErrorMessage.DUPLICATE_EMAIL);
        }

        // 홀수면 남자, 짝수면 여자
        String gender = memberCreateRequest.getGender();

        // 유저 생성 - 학생 또는 교수 구분
        Member member;
        if (memberCreateRequest.getUserType() == 0) { // 학생인 경우
            member = Student.builder()
                    .name(memberCreateRequest.getName())
                    .birth(memberCreateRequest.getBirth())
                    .email(memberCreateRequest.getEmail())
                    .password(passwordEncoder.encode(memberCreateRequest.getPassword()))
                    .gender(memberCreateRequest.getGender())
                    .build();
            return MemberResponse.of(studentRepository.save((Student) member).getId());
        } else if (memberCreateRequest.getUserType() == 1) { // 교수인 경우
            member = Professor.builder()
                    .name(memberCreateRequest.getName())
                    .birth(memberCreateRequest.getBirth())
                    .email(memberCreateRequest.getEmail())
                    .password(passwordEncoder.encode(memberCreateRequest.getPassword()))
                    .gender(memberCreateRequest.getGender())
                    .department(memberCreateRequest.getDepartment())
                    .build();
            return MemberResponse.of(professorRepository.save((Professor) member).getId());
        } else {
            throw new BusinessException(ErrorMessage.InVALID_USERTYPE);
        }
    }

    @Transactional
    public MemberLoginResponse signIn(MemberLoginRequest memberLoginRequest) {
        Member member;
        if (memberLoginRequest.getUserType() == 0) { // 학생인 경우
            member = studentRepository.findByEmailOrElseThrow(memberLoginRequest.getEmail());
        } else if (memberLoginRequest.getUserType() == 1) { // 교수인 경우
            member = professorRepository.findByEmailOrElseThrow(memberLoginRequest.getEmail());
        } else {
            throw new BusinessException(ErrorMessage.InVALID_USERTYPE);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorMessage.INVALID_PASSWORD);
        }

        // AccessToken 발행
        String accessToken = jwtTokenProvider.issueAccessToken(
                UserAuthentication.createUserAuthentication(member.getId())
        );
        String userTypeString = memberLoginRequest.getUserType() == 0 ? "학생" : "교수";

        return MemberLoginResponse.of(accessToken, String.valueOf(member.getId()), userTypeString);
    }

    @Transactional
    public MemberResponse logout(String token) {
        // 이미 로그아웃된 토큰인지 확인
        if (tokenBlacklist.contains(token)) {
            throw new BusinessException(ErrorMessage.ALREADY_LOGOUT_TOKEN);
        }

        // 토큰 유효성 검사
        if (jwtTokenProvider.validateToken(token) != JwtValidationType.VALID_JWT) {
            throw new BusinessException(ErrorMessage.INVALID_JWT_TOKEN);
        }

        // 사용자 존재 여부 확인
        Member member = studentRepository.findById(jwtTokenProvider.getUserFromJwt(token)).orElse(null);
        if (member == null) {
            member = professorRepository.findById(jwtTokenProvider.getUserFromJwt(token))
                    .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER));
        }
        // 블랙리스트에 토큰 추가
        tokenBlacklist.add(token);

        return MemberResponse.of(member.getId());
    }

    @Transactional
    public void deleteAccount(String token) {
        // 블랙리스트에 있는 토큰인지 확인
        if (tokenBlacklist.contains(token)) {
            throw new BusinessException(ErrorMessage.INVALID_JWT_TOKEN); // 이미 로그아웃된 토큰이므로 탈퇴를 진행하지 않음
        }

        // 토큰 유효성 검사
        if (jwtTokenProvider.validateToken(token) != JwtValidationType.VALID_JWT) {
            throw new BusinessException(ErrorMessage.INVALID_JWT_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserFromJwt(token);

        // 사용자 정보 확인 및 계정 삭제
        Member member = studentRepository.findById(userId).orElse(null);
        if (member == null) {
            member = professorRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_USER));
        }

        if (member instanceof Student) {
            studentRepository.delete((Student) member);
        } else if (member instanceof Professor) {
            professorRepository.delete((Professor) member);
        }
    }
}
