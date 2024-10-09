package com.travelock.server.repository;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BigBlockRepository extends JpaRepository<BigBlock, Long> {
}
