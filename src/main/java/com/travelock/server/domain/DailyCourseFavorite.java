package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyCourseFavorite extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyCourseFavoriteId;

    @ManyToOne
    @JoinColumn(name = "dailyCourseFavorites")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "dailyCourseFavorites")
    private DailyCourse dailyCourse;
}
