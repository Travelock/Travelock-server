package com.travelock.server.dto.course.full_create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullCourseCreateDto {
    private Long fullCourseId;
    private String title;
    private List<DailyCourseDto> dailyCourseDtoList;
}
