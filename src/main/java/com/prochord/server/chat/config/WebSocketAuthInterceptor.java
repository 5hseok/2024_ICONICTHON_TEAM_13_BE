//package com.prochord.server.chat.config;
//
//import com.prochord.server.global.jwt.JwtTokenProvider;
//import com.prochord.server.global.jwt.JwtValidationType;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.http.server.ServletServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class WebSocketAuthInterceptor implements HandshakeInterceptor {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        if (request instanceof ServletServerHttpRequest) {
//            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
//            HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
//
//            // Authorization 헤더에서 토큰 추출
//            String authorizationHeader = servletRequest.getHeader("Authorization");
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                String jwtToken = authorizationHeader.substring(7);
//
//                // 토큰 검증
//                JwtValidationType validationResult = jwtTokenProvider.validateToken(jwtToken);
//                if (validationResult == JwtValidationType.VALID_JWT) {
//                    // 유효한 토큰이라면 유저 정보 저장
//                    Long userId = jwtTokenProvider.getUserFromJwt(jwtToken);
//                    attributes.put("userId", userId);
//                    return true;
//                }
//            }
//
//            // 인증 실패 시 401 응답
//            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//            // 응답 본문에 구체적인 에러 메시지 추가
//            servletResponse.setContentType("application/json");
//            servletResponse.setCharacterEncoding("UTF-8");
//
//            String errorMessage = "{ \"error\": \"Unauthorized\", \"message\": \"Invalid or missing Authorization token. Please provide a valid token starting with 'chatUser'.\" }";
//            servletResponse.getWriter().write(errorMessage);
//            servletResponse.getWriter().flush();
//
//            return false;
//        }
//        return false;
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                               WebSocketHandler wsHandler, Exception exception) {
//        // 핸드쉐이크 후 필요한 작업이 있다면 여기에 작성
//    }
//}