package com.travelock.server.dto.course.full_modify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullCourseModifyDto {
    private Long fullCourseId;
    private Long dailyCourseId;
    private Long memberId;
    private Integer dayNum;
}
