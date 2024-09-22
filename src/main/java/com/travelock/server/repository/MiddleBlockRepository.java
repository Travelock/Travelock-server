package com.travelock.server.repository;

import com.travelock.server.domain.MiddleBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiddleBlockRepository extends JpaRepository<MiddleBlock, Long> {
}
