package com.travelock.server.repository;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BigBlockRepository extends JpaRepository<BigBlock, Long> {
    List<BigBlock> findByState(State state);
    // 특정 State 내에 속한 BigBlock 목록 조회
}
