package com.travelock.server.repository;

import com.travelock.server.domain.BigBlock;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BigBlockRepository extends JpaRepository<BigBlock, Long> {
}
