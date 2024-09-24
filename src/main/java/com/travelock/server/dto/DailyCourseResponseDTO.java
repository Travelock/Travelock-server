package com.travelock.server.dto;

import com.travelock.server.domain.FullBlock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyCourseResponseDTO {
    private Long dailyCourseId;
    private Integer dayNum; // 일자 정보(N일차)
    private Long fullCourseId; // 포함되어있는 전체일정 ID
    private Long memberId; // 생성한 멤버 ID
    private Integer favoriteCount; // 좋아요 수
    private Integer scarpCount; // 스크랩 수
    private List<FullBlock> fullBlocks; // 일자별 일정에 포함된 완성 블럭 리스트
}
