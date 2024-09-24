package com.travelock.server.repository;

import com.travelock.server.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {
    State findByStateCode(String stateCode);
    // stateCode로 state 조회하기.
}
