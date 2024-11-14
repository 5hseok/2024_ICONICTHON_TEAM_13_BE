package com.prochord.server.dto.member.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCreateRequest {
    private String email;
    private String password;
    private String name;
    private String birth;
    private Integer gender; // 0이면 남자, 1이면 여자
    private Integer userType; // 0이면 학생, 1이면 교수
    private String number; // 학생이나 교수의 고유 번호
    private String department; // 교수인 경우만 사용
}
