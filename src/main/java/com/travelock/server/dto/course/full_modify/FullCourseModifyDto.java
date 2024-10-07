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
    private Long fullAndDailyConnectId; // -> 프론트에서 연결객체 id를 보내주면 조회가 많이 줄어듬. 전체일정 조회시 응답객체에 추가 고려
}
