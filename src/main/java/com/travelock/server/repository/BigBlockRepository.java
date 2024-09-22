package com.travelock.server.repository;

import com.travelock.server.domain.BigBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BigBlockRepository extends JpaRepository<BigBlock, Long> {
}
