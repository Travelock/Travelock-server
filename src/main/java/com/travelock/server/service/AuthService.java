package com.travelock.server.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.QMember;
import com.travelock.server.exception.auth.ValidateEmailException;
import com.travelock.server.exception.auth.ValidateNickNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JPAQueryFactory query;

    /*이메일 중복 확인*/
    public void validateEmail(String email){
        QMember qMember = QMember.member;

        Integer fetched = query.selectOne().from(qMember).where(qMember.email.eq(email)).fetchFirst();

        if(fetched != null){
            throw new ValidateEmailException("이미 사용중인 이메일");
        }
    }

    public void register(String email, String nickName) {

    }
}
