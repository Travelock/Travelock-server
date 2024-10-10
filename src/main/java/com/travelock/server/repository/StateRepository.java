package com.travelock.server.repository;

import com.travelock.server.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long>{
}

