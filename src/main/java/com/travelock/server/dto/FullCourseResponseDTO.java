package com.travelock.server.dto;

import com.travelock.server.domain.DailyCourse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullCourseResponseDTO {
    private Long fullCourseId;
    private Long memberId; // 생성한 멤버 ID
    private String memberNickName; // 생성한 멤버 닉네임
    private String title; // 전체 일정 타이틀
    private Integer favoriteCount; // 좋아요 수
    private Integer scarpCount; // 스크랩 수
    private List<DailyCourse> dailyCourses;
}
