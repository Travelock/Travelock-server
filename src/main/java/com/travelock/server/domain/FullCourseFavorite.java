package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FullCourseFavorite extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fullCourseFavoriteId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "full_course_id")
    private FullCourse fullCourse;

    public void addFavorite(Member member, FullCourse fullCourse) {
        this.member = member;
        this.fullCourse = fullCourse;
    }
}
