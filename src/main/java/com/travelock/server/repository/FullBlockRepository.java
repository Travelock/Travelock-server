package com.travelock.server.repository;

import com.travelock.server.domain.FullBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FullBlockRepository extends JpaRepository<FullBlock, Long> {
}
