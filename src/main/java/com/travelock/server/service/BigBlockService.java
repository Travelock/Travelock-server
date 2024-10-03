package com.travelock.server.service;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.State;
import com.travelock.server.repository.BigBlockRepository;
import com.travelock.server.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BigBlockService {

    private final BigBlockRepository bigBlockRepository;
    private final StateRepository stateRepository;

    // 빅블럭 조회 메서드
    public BigBlock getBigBlock(String stateCode, String cityCode) {
        log.info("getBigBlock 호출, stateCode={}, cityCode={}", stateCode, cityCode);

        // stateCode로 State 먼저 조회
        State state = stateRepository.findByStateCode(stateCode);
        if (state != null) {
            BigBlock bigBlock = bigBlockRepository.findByStateAndCityCode(state, cityCode);
            if (bigBlock != null) {
                log.info("BigBlock 반환, ID: {}", bigBlock.getBigBlockId());
                return bigBlock;
            } else {
                log.warn("BigBlock을 찾을 수 없음, stateCode={}, cityCode={}", stateCode, cityCode);
                return null;
            }
        } else {
            log.warn("State를 찾을 수 없음, stateCoed={}", stateCode);
            return null;
        }
    }

}