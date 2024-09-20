package com.travelock.server.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.QMember;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.util.GenerateRandomData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final JPAQueryFactory query;
    private final GenerateRandomData generateRandomData;

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

        String provider = query.select(qMember.provider).from(qMember)
                .where(qMember.email.eq(email))
                .fetchOne();
        if (provider == null){
            throw new ResourceNotFoundException("Provider not found in DB by email");
        }
        return provider;
    }
}
