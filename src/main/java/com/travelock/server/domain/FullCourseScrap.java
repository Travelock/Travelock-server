package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FullCourseScrap extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fullCourseScrapId;

    @ManyToOne
    @JoinColumn(name = "fullCourseScraps")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "fullCourseScraps")
    private FullCourse fullCourse;
}
