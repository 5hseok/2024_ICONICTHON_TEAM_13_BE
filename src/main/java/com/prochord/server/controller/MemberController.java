package com.prochord.server.controller;

import com.prochord.server.dto.member.request.MemberCreateRequest;
import com.prochord.server.dto.member.request.MemberLoginRequest;
import com.prochord.server.dto.member.response.MemberLoginResponse;
import com.prochord.server.dto.member.response.MemberResponse;
import com.prochord.server.global.exception.dto.SuccessStatusResponse;
import com.prochord.server.global.exception.message.SuccessMessage;
import com.prochord.server.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<SuccessStatusResponse<MemberResponse>> signUp(@Valid @RequestBody MemberCreateRequest userCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessStatusResponse.of(SuccessMessage.SIGNUP_SUCCESS, userService.signUp(userCreateRequest)));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<SuccessStatusResponse<MemberLoginResponse>> signIn(@Valid @RequestBody MemberLoginRequest userLoginRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessStatusResponse.of(SuccessMessage.SIGNIN_SUCCESS, userService.signIn(userLoginRequest)));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<SuccessStatusResponse<MemberResponse>> logout(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(SuccessStatusResponse.of(SuccessMessage.LOGOUT_SUCCESS, userService.logout(token)));
    }

    // 회원탈퇴
    @DeleteMapping("/users/delete")
    public ResponseEntity<SuccessStatusResponse<Void>> deleteAccount(@RequestHeader("Authorization") String token) {
        userService.deleteAccount(token);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessStatusResponse.of(SuccessMessage.ACCOUNT_DELETION_SUCCESS, null));
    }
}
