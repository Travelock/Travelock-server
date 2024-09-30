package com.travelock.server.repository;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BigBlockRepository extends JpaRepository<BigBlock, Long> {

    // 빅블럭은 단일ID
    BigBlock findByStateAndCityCode(State state, String cityCode);


    // 특정 state 내에 속한 모든 목록 조회
   List<BigBlock> findByState(State state);
}
