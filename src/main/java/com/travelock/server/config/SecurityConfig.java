package com.travelock.server.config;

import com.travelock.server.filter.JWTFilter;
import com.travelock.server.filter.JWTUtil;
import com.travelock.server.oauth2.CustomLogoutSuccessHandler;
import com.travelock.server.oauth2.CustomSuccessHandler;
import com.travelock.server.service.CustomOAuth2UserService;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
//@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private final CustomOAuth2UserService customOAuth2UserService; // 객체 주입

    //20. SuccessHandler 등록
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil, CustomLogoutSuccessHandler customLogoutSuccessHandler) {
        // 클래스 생성자를 통해 위의 객체를 주입
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    //11. oauth2Login 의존성이 데이터를 받았을 때 OauthService의 데이터를 집어 넣어줌
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //22. cors 설정(프론트와 연결)
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        //csrf disable-> jwt를 발급해서 stateless 상태로 관리하려고 하기 때문에, csrf 설정을 끔
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable -> 소셜 로그인을 사용할거기 때문에 해당 설정 끔
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //21-1. JWTFilter 추가
        http
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);


        //oauth2 -> 디폴트 url 을 프론트 url로 설정하면됨
        http
                .oauth2Login((oauth2)->oauth2
                        .loginPage("http://localhost:5173/login")
                        .failureUrl("http://localhost:5173/error")
                        .userInfoEndpoint((userInfoEndpointConfig)->userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler) //20-1.
                );



        http.logout(logout -> logout
                .logoutUrl("/logout")  // 로그아웃 URL 설정
                .logoutSuccessHandler(customLogoutSuccessHandler)  // 커스텀 로그아웃 성공 핸들러 적용
                .invalidateHttpSession(true)  // 세션 무효화
                .clearAuthentication(true)    // 인증 정보 삭제
                .deleteCookies("JSESSIONID", "Authorization", "RefreshToken")  // 특정 쿠키 삭제
        );


        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().permitAll());

        //세션 설정 : STATELESS -> jwt로 인증인가를 진행할거기 때문에 stateless로 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}