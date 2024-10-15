package com.travelock.server.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.converter.DTOConverter;
import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.QBigBlock;
import com.travelock.server.domain.QState;
import com.travelock.server.domain.State;
import com.travelock.server.dto.block.BigBlockResponseDTO;
import com.travelock.server.dto.block.StateResponseDTO;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                city.getState().getStateId(),
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
                        qBigBlock.state.stateId,
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

    public List<BigBlockResponseDTO> getAllCities() {
        QBigBlock qBigBlock = QBigBlock.bigBlock;
        List<BigBlock> bigBlocks = queryFactory.selectFrom(qBigBlock).fetch();

        List<BigBlockResponseDTO> response = new ArrayList<>();

        for(BigBlock b : bigBlocks){
            BigBlockResponseDTO tmp = new BigBlockResponseDTO();
            tmp.setBigBlockId(b.getBigBlockId());
            tmp.setCityName(b.getCityName());
            tmp.setCityCode(b.getCityCode());
            tmp.setStateId(b.getState().getStateId());
            tmp.setStateName(b.getState().getStateName());
            response.add(tmp);
        }
        return response;

    }
}