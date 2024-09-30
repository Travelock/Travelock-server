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

    // 통합
    public BigBlock createOrGetBigBlock(String stateCode, String cityCode) {
        log.info("createOrGetBigBlock 호출, stateCode={}, cityCode={}", stateCode, cityCode);

        // 먼저 State와 관련된 BigBlock이 이미 존재하는지 확인
        State state = stateRepository.findByStateCode(stateCode);
        if (state != null) {
            BigBlock existingBigBlock = bigBlockRepository.findByStateAndCityCode(state, cityCode);
            if (existingBigBlock != null) {  // 기존 BigBlock이 있을 때만 로그 출력
                log.info("기존 BigBlock 반환, ID: {}", existingBigBlock.getBigBlockId());
                return existingBigBlock; // 기존 BigBlock 반환
            } else {
                // 존재하지 않으면 새로 생성
                BigBlock newBigBlock = new BigBlock();
                newBigBlock.setState(state);
                newBigBlock.setCityCode(cityCode);
                newBigBlock.setCityName(getCityNameFromCode(cityCode)); // 도시 이름 매핑
                BigBlock savedBigBlock = bigBlockRepository.save(newBigBlock);
                log.info("새로운 BigBlock 생성, ID: {}", savedBigBlock.getBigBlockId());
                return savedBigBlock;
            }
        } else {
            log.error("State를 State 코드에서 찾을 수 없음 stateCode: {}", stateCode);
            return null;
        }
    }

    private String getCityNameFromCode(String cityCode) {
        log.debug("getCityNameFromCode 호출, cityCode: {}", cityCode);
        return "도시 이름";  // 실제 구현 필요
    }
}