package com.travelock.server.repository;

import com.travelock.server.domain.DailyBlockConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyBlockConnectRepository extends JpaRepository<DailyBlockConnect, Long> {
}
