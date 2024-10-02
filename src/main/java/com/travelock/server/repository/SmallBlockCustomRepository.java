package com.travelock.server.repository;

import com.travelock.server.domain.SmallBlock;

import java.util.Optional;

public interface SmallBlockCustomRepository {
    Optional<SmallBlock> findByPlaceId(String placeId);

}
