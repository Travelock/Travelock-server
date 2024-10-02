package com.travelock.server.repository;

import com.travelock.server.domain.MiddleBlock;

public interface MiddleBlockCustomRepository {
    MiddleBlock findByCategoryCode(String categoryCode);
}
