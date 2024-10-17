package com.travelock.server.dto.oauth2DTO;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao"; // 카카오 로그인 제공자
    }

    @Override
    public String getProviderSecret() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null && kakaoAccount.get("email") != null) {
            return kakaoAccount.get("email").toString();
        }
        return null;
    }


}