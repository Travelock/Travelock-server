package com.travelock.server.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.converter.DTOConverter;
import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.QBigBlock;
import com.travelock.server.domain.QState;
import com.travelock.server.domain.State;
import com.travelock.server.dto.BigBlockResponseDTO;
import com.travelock.server.dto.StateResponseDTO;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.repository.BigBlockRepository;
import com.travelock.server.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BigBlockService {
    private final JPAQueryFactory queryFactory;


    // 전체 시/도 목록 조회 메서드
    public List<StateResponseDTO> getAllStates() {
        log.info("전체 시/도 목록 조회 호출");

        QState qState = QState.state;

        List<State> states = queryFactory
                .selectFrom(qState)
                .fetch();

        return DTOConverter.toDtoList(states, state -> new StateResponseDTO(
                state.getStateId(),
                state.getStateCode(),
                state.getStateName()
        ));
    }

    // 특정 시/도의 시/군/구(BigBlock) "리스트" 조회 ex_경기도에 속한 시 목록
    public List<BigBlockResponseDTO> getCitiesByState(String stateCode) {
        log.info("getCitiesByState 호출, stateCode = {}", stateCode);

        QBigBlock qBigBlock = QBigBlock.bigBlock;
        QState qState = QState.state;

        List<BigBlock> cities = queryFactory
                .selectFrom(qBigBlock)
                .join(qBigBlock.state, qState)
                .where(qState.stateCode.eq(stateCode))
                .fetch();

        return DTOConverter.toDtoList(cities, city -> new BigBlockResponseDTO(
                city.getBigBlockId(),
                city.getCityCode(),
                city.getCityName(),
                city.getState().getStateName()
        ));
    }


    // 특정 시/도에 속한 시/군/구 조회 메서드 // 단일 "시" 조회
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

}