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
    @JoinColumn(name = "fullCourseFavorites")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "fullCourseFavorites")
    private FullCourse fullCourse;
}
