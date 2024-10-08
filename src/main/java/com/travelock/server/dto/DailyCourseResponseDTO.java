package com.travelock.server.dto;

import com.travelock.server.domain.DailyBlockConnect;
import com.travelock.server.domain.DailyCourse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyCourseResponseDTO {
    private Long dailyCourseId;
//    private Integer dayNum;         // 일자 정보(N일차)
    private Long fullCourseId;      // 포함되어있는 전체일정 ID
    private Long memberId;          // 생성한 멤버 ID
    private String memberNickName;  // 생성한 멤버 닉네임 - JK
    private Integer favoriteCount;  // 좋아요 수
    private Integer scarpCount;     // 스크랩 수
    private List<FullBlockDTO> fullBlockList;

    public static DailyCourseResponseDTO fromDomainToResponseDTO(DailyCourse dailyCourse) {
        // FullBlock 리스트 생성 (DailyBlockConnect를 통해 FullBlock을 가져옴)
        List<FullBlockDTO> fullBlockList = new ArrayList<>();
        if (dailyCourse.getDailyBlockConnects() != null) {
            fullBlockList = dailyCourse.getDailyBlockConnects().stream()
                    .map(dailyBlockConnect -> FullBlockDTO // FullBlockDTO로 변환
                            .fromDomainToResponseDTO(dailyBlockConnect.getFullBlock(), dailyBlockConnect.getBlockNum()))
                    .collect(Collectors.toList());
        }


        return new DailyCourseResponseDTO(
                dailyCourse.getDailyCourseId(),
                null,
                dailyCourse.getMember().getMemberId(),
                dailyCourse.getMember().getNickName(),
                dailyCourse.getFavoriteCount(),
                dailyCourse.getScarpCount(),
                fullBlockList
        );
    }
}
