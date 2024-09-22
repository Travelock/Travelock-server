package com.travelock.server.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyCourse implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyCourseId;

    @Column(columnDefinition = "INT COMMENT '일자 정보(N일차)'")
    private Integer dayNum;

    // Daily Course : Member = N : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Daily Course : Full Course = N : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "full_course_id", nullable = false)
    private FullCourse fullCourse;

    // Daily Course : Daily Block Connect = 1 : N
    @OneToMany(mappedBy = "dailyCourse")
    private List<DailyBlockConnect> dailyBlockConnects;

    @OneToMany(mappedBy = "dailyCourse", fetch = FetchType.LAZY)
    private List<DailyCourseFavorite> dailyCourseFavorites;

    @OneToMany(mappedBy = "dailyCourse", fetch = FetchType.LAZY)
    private List<DailyCourseScrap> dailyCourseScraps;
}
