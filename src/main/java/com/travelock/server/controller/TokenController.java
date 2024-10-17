package com.travelock.server.controller;

import com.travelock.server.filter.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TokenController {

    private final JWTUtil jwtUtil;

    public TokenController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue("RefreshToken") String refreshToken, HttpServletResponse response) {
        if (jwtUtil.isExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
        }

        String username=jwtUtil.getName(refreshToken);
        String provider = jwtUtil.getProvider(refreshToken);
        Long memberId = jwtUtil.getMemberId(refreshToken); // memberId 추출
        String newAccessToken = jwtUtil.createJwt(username, provider, "ROLE_USER", memberId, 60 * 60 * 60L);

        // HttpOnly 쿠키로 Access Token 설정
        Cookie accessTokenCookie = new Cookie("Authorization", newAccessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(60 * 60); // 1시간 만료시간
        accessTokenCookie.setPath("/"); // 모든 경로에서 사용 가능
        response.addCookie(accessTokenCookie);

        // 새로운 리프레시 토큰이 필요한 경우 추가 생성 가능
        // 리프레시 토큰도 HttpOnly 쿠키로 설정하는 방식으로 구현 가능

        return ResponseEntity.ok().build(); // 토큰은 쿠키로 전송되므로 별도로 리턴할 필요 없음
    }
}
