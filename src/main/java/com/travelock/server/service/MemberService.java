package com.travelock.server.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.QMember;
import com.travelock.server.util.GenerateRandomData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final JPAQueryFactory query;
    private final GenerateRandomData generateRandomData;

    public void leave(Long memberId){
        String rEmail = generateRandomData.generateRandomEmail();
        String rNickName = generateRandomData.generateRandomNickName();

        QMember qMember = QMember.member;
        long result = query.update(qMember)
                .set(qMember.email, rEmail)
                .set(qMember.nickName, rNickName)
                .where(qMember.memberId.eq(memberId))
                .execute();
    }

    public String getProvider(String email){
        QMember qMember = QMember.member;

        return query.select(qMember.provider).from(qMember)
                .where(qMember.email.eq(email))
                .fetchOne();
    }
}
