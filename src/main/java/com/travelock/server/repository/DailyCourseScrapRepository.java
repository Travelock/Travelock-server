package com.travelock.server.repository;

import com.travelock.server.domain.DailyCourseScrap;
import com.travelock.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyCourseScrapRepository extends JpaRepository<DailyCourseScrap, Long> {
}
