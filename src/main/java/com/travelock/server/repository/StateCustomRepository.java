package com.travelock.server.repository;

import com.travelock.server.domain.State;

public interface StateCustomRepository {
    State findByStateCode(String stateCode);

}
