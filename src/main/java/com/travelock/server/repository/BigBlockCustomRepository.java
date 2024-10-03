package com.travelock.server.repository;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.State;

import java.util.List;

public interface BigBlockCustomRepository {
    BigBlock findByStateAndCityCode(State state, String cityCode);
    List<BigBlock> findByState(State state);
}
