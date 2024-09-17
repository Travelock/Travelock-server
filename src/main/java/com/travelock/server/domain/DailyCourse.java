package com.travelock.server.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyCourse {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyCourseId;

    @OneToMany(mappedBy = "dailyCourse", fetch = FetchType.LAZY)
    private List<DailyCourseFavorite> dailyCourseFavorites;

    @OneToMany(mappedBy = "dailyCourse", fetch = FetchType.LAZY)
    private List<DailyCourseScrap> dailyCourseScraps;
}
