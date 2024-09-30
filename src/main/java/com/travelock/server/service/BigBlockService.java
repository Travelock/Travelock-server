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

    public BigBlock createBigBlock(String stateCode, String cityCode) {
        log.info("Create BigBlock 호출, stateCode={}, cityCode={}", stateCode, cityCode);
        State state = stateRepository.findByStateCode(stateCode);
        if (state != null) {
            BigBlock bigBlock = new BigBlock();
            bigBlock.setState(state);
            bigBlock.setCityCode(cityCode);
            bigBlock.setCityName(getCityNameFromCode(cityCode));
            return bigBlockRepository.save(bigBlock);
        }
        log.error("State를 State코드에서 찾을 수 없음 stateCode: {}", stateCode);
        return null;
    }

    private String getCityNameFromCode(String cityCode) {
        log.debug("getCityNameFromCode 호출, cityCode: {}", cityCode);
        return "도시 이름 테스트";
    }
}
