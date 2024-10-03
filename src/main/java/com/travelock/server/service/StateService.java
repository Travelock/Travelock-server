package com.travelock.server.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.converter.DTOConverter;
import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.QBigBlock;
import com.travelock.server.domain.QState;
import com.travelock.server.domain.State;
import com.travelock.server.dto.BigBlockResponseDTO;
import com.travelock.server.dto.StateResponseDTO;
import com.travelock.server.repository.BigBlockRepository;
import com.travelock.server.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StateService {
    private final JPAQueryFactory queryFactory;
    private final StateRepository stateRepository;
    private final BigBlockRepository bigBlockRepository;

    // 전체 시/도 목록 조회
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

    // 특정 시/도의 시/군/구(BigBlock) 목록 조회
    public List<BigBlockResponseDTO> getCitiesByState(String stateCode) {
        log.info("getCitiesByState 호출, stateCode = {}", stateCode);

        QBigBlock qBigBlock = QBigBlock.bigBlock;
        QState qState = QState.state;

        List<BigBlock> cities = queryFactory
                .selectFrom(qBigBlock)
                .join(qBigBlock.state, qState)
                .where(qState.stateCode.eq(stateCode))
                .fetch();

        // DTOConverter를 사용해 엔티티를 DTO로 변환
        return DTOConverter.toDtoList(cities, city -> new BigBlockResponseDTO(
                city.getBigBlockId(),
                city.getCityCode(),
                city.getCityName(),
                city.getState().getStateName()
        ));
    }

//    // 특정 시/도의 시,구 목록 조회
//    public List<BigBlock> getCitiesByState(String stateCode) {
//        log.info("getCitiesByState 호출, stateCode: {}", stateCode);
//        State state = stateRepository.findByStateCode(stateCode);
//        if (state != null) {
//            return bigBlockRepository.findByState(state);
//        }
//        log.warn("State not found for stateCode: {}", stateCode);
//        return new ArrayList<>();
//
//    }
}

