package com.travelock.server.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.MiddleBlock;
import com.travelock.server.domain.QMiddleBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MiddleBlockRepositoryImpl implements MiddleBlockCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public MiddleBlock findByCategoryCode(String categoryCode) {
        QMiddleBlock middleBlock = QMiddleBlock.middleBlock;

        return queryFactory
                .selectFrom(middleBlock)
                .where(middleBlock.categoryCode.eq(categoryCode))
                .fetchOne();
    }

}
