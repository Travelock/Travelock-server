package com.travelock.server.repository;

import com.travelock.server.domain.FullCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FullCourseRepository extends JpaRepository<FullCourse, Long> {
}
