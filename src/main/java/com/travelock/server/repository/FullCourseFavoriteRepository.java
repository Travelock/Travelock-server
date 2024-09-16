package com.travelock.server.repository;

import com.travelock.server.domain.FullCourseFavorite;
import com.travelock.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FullCourseFavoriteRepository extends JpaRepository<FullCourseFavorite, Long> {
}
