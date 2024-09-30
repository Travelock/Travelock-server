package com.travelock.server.service;

import com.opencsv.CSVReader;
import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.State;
import com.travelock.server.repository.BigBlockRepository;
import com.travelock.server.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CSVImportService {

    private final StateRepository stateRepository;
    private final BigBlockRepository bigBlockRepository;

    @Transactional
    public void importCSV(String filePath) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();

            // 첫 번째 레코드가 헤더일 경우 건너뛰기
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                String stateName = record[0];  // 시도명
                String cityName = record[1];   // 시군구명
                String stateCode = record[2];   // stateCode
                String cityCode = record[3];    // cityCode

                // State 찾기 또는 새로 생성
                if (stateCode.length() > 2) {
                    throw new Exception("State code is too long: " + stateCode);
                }

                State state = stateRepository.findByStateCode(stateCode);
                if (state == null) {
                    state = new State();
                    state.setStateCode(stateCode);
                    state.setStateName(stateName);
                    stateRepository.save(state);
                }

                // BigBlock 생성
                if (cityName != null && !cityName.isEmpty()) {
                    BigBlock bigBlock = new BigBlock();
                    bigBlock.setState(state);
                    bigBlock.setCityCode(cityCode);
                    bigBlock.setCityName(cityName);
                    bigBlockRepository.save(bigBlock);
                }
            }
        } catch (Exception e) {
            throw new Exception("CSV 처리 중 오류 발생: " + e.getMessage());
        }
    }
}