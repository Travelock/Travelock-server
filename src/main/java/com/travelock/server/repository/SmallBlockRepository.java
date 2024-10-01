package com.travelock.server.repository;

import com.travelock.server.domain.SmallBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SmallBlockRepository extends JpaRepository<SmallBlock, Long> {
    Optional<SmallBlock> findByPlaceId(String placeId);

}
