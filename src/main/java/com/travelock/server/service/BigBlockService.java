package com.travelock.server.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.converter.DTOConverter;
import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.QBigBlock;
import com.travelock.server.domain.QState;
import com.travelock.server.dto.BigBlockResponseDTO;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.repository.BigBlockRepository;
import com.travelock.server.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BigBlockService {
    private final JPAQueryFactory queryFactory;
    private final BigBlockRepository bigBlockRepository;
    private final StateRepository stateRepository;


    public BigBlockResponseDTO getBigBlock(String stateCode, String cityCode) {
        QBigBlock qBigBlock = QBigBlock.bigBlock;
        QState qState = QState.state;

        BigBlockResponseDTO bigBlock = queryFactory
                .select(Projections.constructor(BigBlockResponseDTO.class,
                        qBigBlock.bigBlockId,
                        qBigBlock.cityCode,
                        qBigBlock.cityName,
                        qBigBlock.state.stateName))
                .from(qBigBlock)
                .join(qBigBlock.state, qState)
                .where(
                        qBigBlock.cityCode.eq(cityCode),
                        qState.stateCode.eq(stateCode)
                )
                .fetchOne();

        if (bigBlock == null) {
            throw new ResourceNotFoundException("BigBlock not found for stateCode: " + stateCode + " and cityCode: " + cityCode);
        }

        return bigBlock;
    }

    //    // QueryDSL로 BigBlock 조회 후 DTOConverter를 사용하여 DTO로 변환
//    public BigBlockResponseDTO getBigBlock(String stateCode, String cityCode) {
//        QBigBlock bigBlock = QBigBlock.bigBlock;
//        QState state = QState.state;
//
//        // QueryDSL로 BigBlock 엔티티를 조회
//        BigBlock entity = queryFactory
//                .selectFrom(bigBlock)
//                .join(bigBlock.state, state)
//                .where(
//                        bigBlock.cityCode.eq(cityCode),
//                        state.stateCode.eq(stateCode)
//                )
//                .fetchOne();
//
//        if (entity == null) {
//            throw new ResourceNotFoundException("BigBlock not found for stateCode: " + stateCode + " and cityCode: " + cityCode);
//        }
//
//        // DTOConverter를 사용하여 BigBlock 엔티티를 BigBlockResponseDTO로 변환
//        return DTOConverter.toDto(entity, block -> new BigBlockResponseDTO(
//                block.getBigBlockId(),
//                block.getCityCode(),
//                block.getCityName(),
//                block.getState().getStateName()
//        ));
//    }

}