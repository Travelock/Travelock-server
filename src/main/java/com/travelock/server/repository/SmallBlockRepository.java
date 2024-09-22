package com.travelock.server.repository;

import com.travelock.server.domain.SmallBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmallBlockRepository extends JpaRepository<SmallBlock, Long> {
}
