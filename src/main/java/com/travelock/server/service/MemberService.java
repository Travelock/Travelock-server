package com.travelock.server.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.Member;
import com.travelock.server.domain.QMember;
import com.travelock.server.dto.oauth2DTO.MemberDTO;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.filter.JWTUtil;
import com.travelock.server.repository.MemberRepository;
import com.travelock.server.util.CurrentMember;
import com.travelock.server.util.GenerateRandomData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final JPAQueryFactory query;
    private final GenerateRandomData generateRandomData;

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;
    private final CurrentMember currentMember;


    public void leave(Long memberId){
        try {
            String rEmail = generateRandomData.generateRandomEmail();
            String rNickName = generateRandomData.generateRandomNickName();

            QMember qMember = QMember.member;
            query.update(qMember)
                    .set(qMember.email, rEmail)
                    .set(qMember.nickName, rNickName)
                    .set(qMember.active_status, "n")
                    .where(qMember.memberId.eq(memberId))
                    .execute();
        }catch (Exception e){
            log.error("회원탈퇴 실패, 오류 발생", e);
        }

    }

    public String getProvider(String email){
        QMember qMember = QMember.member;

        String provider = query.select(qMember.username).from(qMember)
                .where(qMember.email.eq(email))
                .fetchOne();
        if (provider == null){
            throw new ResourceNotFoundException("Provider not found in DB by email");
        }
        return provider;
    }


    // 닉네임 체크 로직
    public MemberDTO checkNickName(String token) {
        // JWT 토큰에서 memberId 추출
        Long memberId = jwtUtil.getMemberId(token);

        if (memberId == null) {
            throw new IllegalArgumentException("JWT에서 추출한 memberId가 null입니다.");
        }

        // memberId로 DB에서 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        // Member -> MemberDTO로 변환
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setNickname(member.getNickName());
        return memberDTO;
    }

    // 닉네임 중복 여부 확인 메서드
    public boolean existsByNickName(String nickName) {
        return memberRepository.existsByNickName(nickName);  // 리포지토리에서 닉네임 중복 체크
    }

    // 닉네임 저장 메서드 (토큰에서 정보를 추출하여 회원을 조회)
    public void saveNickName(String token, String nickName) {
        // JWT 토큰에서 memberId 추출
        Long memberId = jwtUtil.getMemberId(token);

        // memberId로 DB에서 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        // 닉네임 저장
        member.setNickName(nickName);
        memberRepository.save(member);
    }

    // 닉네임 수정 로직 추가
    public void updateNickName(String token, String newNickName) {
        Long memberId = jwtUtil.getMemberId(token); // JWT에서 memberId 추출
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 닉네임 수정
        member.setNickName(newNickName);
        memberRepository.save(member); // 수정된 닉네임 저장
    }





    public Member getInfo() {
        return currentMember.getMember();
    }


}
