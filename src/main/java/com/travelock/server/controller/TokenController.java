package com.travelock.server.controller;

import com.travelock.server.filter.JWTUtil;
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
    public ResponseEntity<?> refreshToken(@CookieValue("RefreshToken") String refreshToken) {
        if (jwtUtil.isExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
        }

        String username = jwtUtil.getUsername(refreshToken);
        Number memberId = jwtUtil.getMemberId(refreshToken);
        String newAccessToken = jwtUtil.createJwt(username, "ROLE_USER", 60*60*60L, (Long) memberId); // 새로운 액세스 토큰 발급

        // 새로운 액세스 토큰을 반환
        return ResponseEntity.ok().header("Authorization", newAccessToken).build();
        // 수정수정, 필터도 수정수정 -> 이해 안가서 다시 물어보기
        // 헤더에 넣지 말고, 쿠키에 넣어서 사용(httponly) -> 토큰만 리턴하게 하고, 필터에서 쿠키를 만드러 req,res 객체로 전달하기.
    }
}
