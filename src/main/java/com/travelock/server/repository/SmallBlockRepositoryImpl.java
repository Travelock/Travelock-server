package com.travelock.server.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.QSmallBlock;
import com.travelock.server.domain.SmallBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SmallBlockRepositoryImpl implements SmallBlockCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<SmallBlock> findByPlaceId(String placeId) {
        QSmallBlock smallBlock = QSmallBlock.smallBlock;

        SmallBlock result = queryFactory
                .selectFrom(smallBlock)
                .where(smallBlock.placeId.eq(placeId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

}
