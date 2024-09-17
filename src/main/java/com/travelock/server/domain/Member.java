package com.travelock.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Size(max = 100) @Column(nullable = false, unique = true, columnDefinition = "varchar(100)")
    private String email;
    @Size(max = 50) @Column(unique = true, columnDefinition = "varchar(50)")
    private String nickName;
    @Size(max = 50) @Column(columnDefinition = "varchar(50)")
    private String provider;
    @Size(max = 1) @Column(columnDefinition = "varchar(1)")
    private String active_status;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<SmallBlockReview> smallBlockReviews;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<DailyCourseFavorite> dailyCourseFavorites;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<DailyCourseScrap> dailyCourseScraps;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<FullCourseFavorite> fullCourseFavorites;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<FullCourseScrap> fullCourseScraps;

    public void register(String email){
        this.email = email;
        this.active_status = "y";
    }


}
