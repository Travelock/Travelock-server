package com.travelock.server.dto.course.cache;

import com.travelock.server.dto.block.FullBlockResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyCourseCacheDto {
    private Long dailyCourseId;
    private Integer favoriteCount;
    private Integer scrapCount;
    private Long memberId;
    private String memberNickName;
}
