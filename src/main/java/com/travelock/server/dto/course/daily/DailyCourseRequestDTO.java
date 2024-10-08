package com.travelock.server.dto.course.daily;

import com.travelock.server.dto.block.FullBlockRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyCourseRequestDTO {
    private Long fullCourseId;      // 포함되어있는 전체일정 ID
    private Long memberId;          // 생성한 멤버 ID
    private Integer dayNum;         // 일자 정보(N일차)
    private Long dailyCourseId;     // -> 수정시 사용, 생성시 null처리
    private List<FullBlockRequestDTO> fullBlockDtoList;
    private List<Long> dailyBlockConnectIds; // -> 수정시 사용
}
