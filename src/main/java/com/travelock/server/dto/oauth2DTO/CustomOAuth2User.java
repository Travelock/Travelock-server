package com.travelock.server.dto.oauth2DTO;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final MemberDTO memberDTO;
    public CustomOAuth2User(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return null; // 구글과 네이버의 attributes의 값들이 다르기 때문에 null 처리
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(memberDTO.getRole()));  // 사용자 역할 반환
        return authorities;
    }

    @Override
    public String getName() {
        // memberDTO.getName()이 null이거나 비어있으면 예외 발생
        if (memberDTO.getUsername() == null || memberDTO.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return memberDTO.getUsername();
    }

    public String getProvider() {
        return  memberDTO.getProvider();
    }

//    public Long getMemberId() {return memberDTO.getMemberId();}

    public Long getMemberId() {
        if (memberDTO.getMemberId() == null) {
            throw new IllegalStateException("MemberId is null in CustomOAuth2User");
        }
        return memberDTO.getMemberId(); // MemberDTO에서 memberId 반환
    }



}
