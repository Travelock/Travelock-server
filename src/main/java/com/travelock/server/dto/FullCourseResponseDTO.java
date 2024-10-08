package com.travelock.server.dto;

import com.travelock.server.domain.FullCourse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
    private List<DailyCourseResponseDTO> dailyCourses;
    private List<Long> fullAndDailyConnectIds; // -> 수정시 사용

    public static FullCourseResponseDTO fromDomainToResponseDTO(FullCourse fullCourse) {
        // DailyCourse List 생성
        List<DailyCourseResponseDTO> dailyCourseList = new ArrayList<>();
        List<Long> fullAndDailyConnectIds = new ArrayList<>();
        if (fullCourse.getFullAndDailyCourseConnects() != null) {
            dailyCourseList = fullCourse.getFullAndDailyCourseConnects().stream()
                    .map(fullAndDailyCourseConnect -> DailyCourseResponseDTO // DailyCourseResponseDTO로 변환
                            .fromDomainToResponseDTO(fullAndDailyCourseConnect.getDailyCourse()))
                    .collect(Collectors.toList());

        }
        if(fullCourse.getFullAndDailyCourseConnects() != null) {
            //전체일정 조회시 Dto에 연결객체 Id목록 추가
            fullAndDailyConnectIds = fullCourse.getFullAndDailyCourseConnects().stream()
                    .map(fullAndDailyCourseConnect -> fullAndDailyCourseConnect.getDailyCourse().getDailyCourseId())
                    .collect(Collectors.toList());
        }

        return new FullCourseResponseDTO(
                fullCourse.getFullCourseId(),
                fullCourse.getMember().getMemberId(),
                fullCourse.getMember().getNickName(),
                fullCourse.getTitle(),
                fullCourse.getFavoriteCount(),
                fullCourse.getScarpCount(),
                dailyCourseList,
                fullAndDailyConnectIds
        );
    }
}
