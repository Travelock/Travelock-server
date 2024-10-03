package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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


    public void createNewConnect(Member member, FullCourse fullCourse, DailyCourse dailyCourse, Integer dailyNum){
        this.member = member;
        this.fullCourse = fullCourse;
        // @TODO Full - Daily 연결시, full이랑 daily랑 둘 다 필수값
        this.dailyCourse = dailyCourse;
        this.dailyNum = dailyNum;
    }
}
