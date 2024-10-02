package com.travelock.server.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.QBigBlock;
import com.travelock.server.domain.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BigBlockRepositoryImpl implements BigBlockCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public BigBlock findByStateAndCityCode(State state, String cityCode) {
        QBigBlock bigBlock = QBigBlock.bigBlock;

        return queryFactory
                .selectFrom(bigBlock)
                .where(bigBlock.state.eq(state)
                        .and(bigBlock.cityCode.eq(cityCode)))
                .fetchOne();
    }

    @Override
    public List<BigBlock> findByState(State state) {
        QBigBlock bigBlock = QBigBlock.bigBlock;

        return queryFactory
                .selectFrom(bigBlock)
                .where(bigBlock.state.eq(state))
                .fetch();
    }
}
