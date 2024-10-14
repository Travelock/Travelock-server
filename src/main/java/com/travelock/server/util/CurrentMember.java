package com.travelock.server.util;

import com.travelock.server.domain.Member;
import com.travelock.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentMember {
    private final MemberRepository memberRepository;
    public Member getMember(){
        //임시
        Long memberId = 1L;
        return memberRepository.findById(memberId).orElseThrow(()-> new UsernameNotFoundException("Member not found"));
    }
}
