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
    @JoinColumn(name = "dailyCourseScraps")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "dailyCourseScraps")
    private DailyCourse dailyCourse;


}
