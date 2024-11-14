package com.prochord.server.dto.member.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginRequest {
    private String email;
    private String password;
    private Integer userType; // 0이면 학생, 1이면 교수
}
