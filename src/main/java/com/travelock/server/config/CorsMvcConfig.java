package com.travelock.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class CorsMvcConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")  // 모든 도메인 허용, 인증 정보 포함 가능
//                .allowedMethods("*")  // 모든 HTTP 메서드 허용
//                .allowedHeaders("*")  // 모든 헤더 허용
//                .allowCredentials(true);
//
//    }
//}

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    // CORS 설정을 추가하기 위한 메소드
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**") // 서버의 모든 API 경로에 대해 CORS 적용
                .exposedHeaders("Set-Cookie")
                // 기본적으로 CORS 응답에서는 일부 헤더가 클라이언트에 노출되지 않는데, 해당 설정을 통해 클라이언트가 응답에서 Set-Cookie 헤더를 읽을 수 있도록 허용
                .allowedOrigins("http://localhost:5173") // 해당 경로에서 오는 요청만 허용 (해당 경로에서 서버로 API 요청을 보낼 수 O)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 서버가 허용하는 HTTP 메소드
                .allowCredentials(true) ;// 자격 증명을 포하한 요청을 허용 (쿠키, 인증 헤더, SSL 인증서 등을 허용)
        // .allowedHeaders("*"); // HTTP 요청에서 헤더는 메타 데이터를 담고 있다. -> *는 모든 헤더를 제한 없이 허용
    }
}
