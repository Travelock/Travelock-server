//21. JWT를 검증, 해당 필터를 통해 요청 쿠키에 JWT가 존재하는 경우 JWT를 검증하고 강제로SecurityContextHolder에 세션을 생성한다.
// (이 세션은 STATLESS 상태로 관리되기 때문에 해당 요청이 끝나면 소멸 된다.)
package com.travelock.server.filter;

import com.travelock.server.dto.oauth2DTO.CustomOAuth2User;
import com.travelock.server.dto.oauth2DTO.MemberDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 특정 경로(/api 등)는 인증 없이도 접근 가능하도록 예외 처리
        if (request.getServletPath().contains("/t1")) {
            System.out.println("filter passed::::::::::::::::");
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 Authorization 토큰 추출
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            System.out.println("No cookies found");
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = null;
        for (Cookie cookie : cookies) {
            if ("Authorization".equals(cookie.getName())) {
                authorization = cookie.getValue();
                break;
            }
        }

        // Authorization 쿠키가 없으면 필터 체인 진행
        if (authorization == null) {
            System.out.println("Token not found in cookies");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization;

        // 토큰 만료 여부 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("Token expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 유효한 경우, 토큰에서 필요한 정보 추출

        String provider = jwtUtil.getProvider(token);
        String role = jwtUtil.getRole(token);
        Long memberId = jwtUtil.getMemberId(token);

        // 회원 정보를 담은 DTO 생성
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setProvider(provider);
        memberDTO.setRole(role);
        memberDTO.setMemberId(memberId);

        // CustomOAuth2User에 회원 정보 세팅
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDTO);

        // Spring Security 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // SecurityContextHolder에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}