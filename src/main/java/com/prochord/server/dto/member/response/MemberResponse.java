package com.prochord.server.dto.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {
    private Long memberId;

    public static MemberResponse of(Long memberId) {
        return new MemberResponse(memberId);
    }
}