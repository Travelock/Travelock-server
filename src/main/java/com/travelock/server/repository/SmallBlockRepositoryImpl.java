package com.travelock.server.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.QSmallBlock;
import com.travelock.server.domain.SmallBlock;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
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

        public SmallBlock getSmallBlock(String placeId) {
        QSmallBlock qSmallBlock = QSmallBlock.smallBlock;

        SmallBlock smallblock = queryFactory
                .selectFrom(qSmallBlock)
                .where(qSmallBlock.placeId.eq(placeId))
                .fetchOne();
        if(qSmallBlock == null){
            throw new ResourceNotFoundException("SmallBlock not found.");
        }
            
        return smallblock;
    }

}
