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
        // 로그를 추가하여 memberId가 제대로 설정되었는지 확인
        if (memberDTO.getMemberId() == null) {
            System.out.println("Error: MemberId is null in CustomOAuth2User constructor");
        } else {
            System.out.println("Success: MemberId in CustomOAuth2User constructor is " + memberDTO.getMemberId());
        }
    }

    @Override
    public String getName() {
        // 이 메서드는 사용자의 고유 식별자를 반환해야 합니다. -> OAuth2User의 필수 메서드라 memberid를 반환하도록 설정함
        return memberDTO.getMemberId().toString();
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
