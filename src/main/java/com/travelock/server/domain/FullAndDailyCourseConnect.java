package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FullAndDailyCourseConnect {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fullDailyCourseConnectId;
    private Integer dailyNum;

    // Full Course Connect : Full Block = N : 1
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Full Course Connect : Full Block = N : 1
    @ManyToOne
    @JoinColumn(name = "full_course_id", nullable = false)
    private FullCourse fullCourse;

    // Daily Course Connect : Full Block = N : 1
    @ManyToOne
    @JoinColumn(name = "daily_course_id", nullable = false)
    private DailyCourse dailyCourse;


    public void createNewConnect(Member member, FullCourse fullCourse, Integer dailyNum){
        this.member = member;
        this.fullCourse = fullCourse;
        this.dailyNum = dailyNum;
    }
}
