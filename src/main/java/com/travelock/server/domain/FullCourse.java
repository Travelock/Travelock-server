package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FullCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fullCourseId;

    @OneToMany(mappedBy = "fullCourse", fetch = FetchType.LAZY)
    private List<FullCourseFavorite> fullCourseFavorites;

    @OneToMany(mappedBy = "fullCourse", fetch = FetchType.LAZY)
    private List<FullCourseScrap> fullCourseScraps;
}
