package com.travelock.server.repository;

import com.travelock.server.domain.SmallBlock;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SmallBlockRepository extends JpaRepository<SmallBlock, Long> {
    SmallBlock findByPlaceId(String placeId);

}
