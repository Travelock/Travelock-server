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
public class FullCourse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fullCourseId;

    @Column(columnDefinition = "VARCHAR(100) NULL COMMENT '여행 타이틀'")
    private String title;

    @Column(columnDefinition="INT COMMENT '좋아요 수'")
    private Integer favoriteCount;

    @Column(columnDefinition = "INT COMMENT '스크랩 수'")
    private Integer scarpCount;

    @Column(columnDefinition = "VARCHAR(1) COMMENT '활성화 상태'")
    private String activeStatus;

    // Full Course : Member = N : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Full Course : Daily Course = 1 : N
    @OneToMany(mappedBy = "fullCourse")
    private List<DailyCourse> dailyCourses;

    @OneToMany(mappedBy = "fullCourse", fetch = FetchType.LAZY)
    private List<FullCourseFavorite> fullCourseFavorites;

    @OneToMany(mappedBy = "fullCourse", fetch = FetchType.LAZY)
    private List<FullCourseScrap> fullCourseScraps;
}
