package com.travelock.server.service;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.State;
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
    private final StateRepository stateRepository;
    private final BigBlockRepository bigBlockRepository;

    // 전체 시,도 목록 조회하기
    public List<State> getAllStates() {
        log.info("모든 states 호출");
        return stateRepository.findAll();
    }

    // 특정 시/도의 시,구 목록 조회
    public List<BigBlock> getCitiesByState(String stateCode) {
        log.info("getCitiesByState 호출, stateCode: {}", stateCode);
        State state = stateRepository.findByStateCode(stateCode);
        if (state != null) {
            return bigBlockRepository.findByState(state);
        }
        log.warn("State not found for stateCode: {}", stateCode);
        return new ArrayList<>();

    }
}

