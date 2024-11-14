package com.prochord.server.dto.member.response;

public record MemberLoginResponse(
    String accessToken,
    String userId,
    String userName
) {

    public static MemberLoginResponse of(
        String accessToken,
        String userId,
        String userName,
        String userType
    ) {
        return new MemberLoginResponse(accessToken, userId, userName);
    }
}