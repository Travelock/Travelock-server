package com.travelock.server.repository;

import com.travelock.server.domain.DailyCourseFavorite;
import com.travelock.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyCourseFavoriteRepository extends JpaRepository<DailyCourseFavorite, Long> {
}
