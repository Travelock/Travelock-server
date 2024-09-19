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
    @JoinColumn(name = "daily_course_favorites_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "daily_course_favorites_id")
    private DailyCourse dailyCourse;

    public void addFavorite(Member member, DailyCourse dailyCourse){
        this.member = member;
        this.dailyCourse = dailyCourse;
    }
}
