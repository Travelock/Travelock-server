package com.travelock.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration

//@EnableWebSecurity(debug = true) http관련 상세 디버깅용
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
}
