package com.travelock.server.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.QState;
import com.travelock.server.domain.State;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StateRepositoryImpl implements StateCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public State findByStateCode(String stateCode) {
        QState state = QState.state;

        return queryFactory
                .selectFrom(state)
                .where(state.stateCode.eq(stateCode))
                .fetchOne();
    }
}
