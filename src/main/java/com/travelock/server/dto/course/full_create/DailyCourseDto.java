package com.travelock.server.dto.course.full_create;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyCourseDto {
    private Long dailyCourseId;
    private Integer dailyCourseNum;
}
