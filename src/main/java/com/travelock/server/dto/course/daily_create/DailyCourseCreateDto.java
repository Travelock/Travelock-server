package com.travelock.server.dto.course.daily_create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyCourseCreateDto {
    private Integer dayNum;
    private Long memberId;
    private Long fullCourseId;
    private Long dailyCourseId; // -> 수정시 사용, 생성시 null처리
    private List<FullBlockDto> fullBlockDtoList;
}
