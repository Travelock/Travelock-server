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

    @Column(columnDefinition="INT NOT NULL DEFAULT 0 COMMENT '좋아요 수'")
    private Integer favoriteCount;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0 COMMENT '스크랩 수'")
    private Integer scarpCount;

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

    /**
     * DailyCourse Set Function
     */
    public void addDailyCourse(Integer dayNum, Member member, FullCourse fullCourse) {
        this.dayNum = dayNum;
        this.member = member;
        this.fullCourse = fullCourse;

        // 좋아요, 스크랩 수 기본값
        this.favoriteCount = 0;
        this.scarpCount = 0;
    }
}
