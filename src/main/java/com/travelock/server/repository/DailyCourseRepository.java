package com.travelock.server.repository;

import com.travelock.server.domain.DailyCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyCourseRepository extends JpaRepository<DailyCourse, Long> {
}
