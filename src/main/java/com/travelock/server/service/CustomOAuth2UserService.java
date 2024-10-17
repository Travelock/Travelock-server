
package com.travelock.server.service;

import com.travelock.server.domain.Member;
import com.travelock.server.dto.oauth2DTO.*;
import com.travelock.server.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

// MemberService 대체!
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    // SecurityConfig의 oauth2login메소드에 등록을 하여 사용
    // DefaultOAuth2UserService : 유저정보를 획득하기 위한 함수들

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2UserRequest : 제공되는 유저정보

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // registrationId : 구글인지 네이버인지 카카오인지 확인되는 값

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        // 리소스 서버에서 받은 데이터를 OauthService에 전달되면 OauthUser의 dto에 담아 프로바이드에 전달해주면 로그인 완료
        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬 (프로바이더와 해당 id 값을 받아옴)
        String provider = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderSecret();

        Member existData = memberRepository.findByProvider(provider); // 해당 유저가 존재하는지 조회


        if (existData == null) { // 한번도 로그인을 하지 않은 경우


            Member member = new Member();
            member.setProvider(provider);
            member.setEmail(oAuth2Response.getEmail());
            member.setRole("ROLE_USER");

            Member savedMember = memberRepository.save(member);


            MemberDTO memberDTO = new MemberDTO(); //MemberDTO에 데이터 담기
            memberDTO.setProvider(provider);
            memberDTO.setRole("ROLE_USER");
            memberDTO.setMemberId(savedMember.getMemberId());

            return new CustomOAuth2User(memberDTO);
        }
        else { // 한번이라도 로그인을 해서 데이터가 존재하는 경우

            existData.setEmail(oAuth2Response.getEmail()); // 데이터 업데이트


            Member updatedMember = memberRepository.save(existData); // 업데이트 후 저장된 객체 반환

            MemberDTO memberDTO = new MemberDTO(); // 특정한 dto에 담아 CustomOAuth2User에 넘겨주면 로그인 완료
            memberDTO.setProvider(existData.getProvider());
            memberDTO.setRole(existData.getRole());
            memberDTO.setMemberId(updatedMember.getMemberId());

            return new CustomOAuth2User(memberDTO);
        }

    }
}