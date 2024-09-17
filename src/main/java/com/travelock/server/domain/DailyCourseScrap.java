package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyCourseScrap extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyCourseScrapId;

    @ManyToOne
    @JoinColumn(name = "daily_course_scraps_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "daily_course_scraps_id")
    private DailyCourse dailyCourse;


    public void addScrap(Member member, DailyCourse dailyCourse) {
        this.member = member;
        this.dailyCourse = dailyCourse;
    }
}
