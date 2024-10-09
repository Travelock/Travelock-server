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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_course_id")
    private DailyCourse dailyCourse;

    public void addFavorite(Member member, DailyCourse dailyCourse){
        this.member = member;
        this.dailyCourse = dailyCourse;
    }
}
