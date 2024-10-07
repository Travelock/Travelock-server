package com.travelock.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullCourseRequestDTO {
    private Long fullCourseId;
    private Long memberId; // 생성한 멤버 ID
    private String title; // 전체 일정 타이틀
    private Integer favoriteCount; // 좋아요 수
    private Integer scarpCount; // 스크랩 수
}