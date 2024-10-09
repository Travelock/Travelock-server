package com.travelock.server.dto.course.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullCourseCacheDto {
    private Long fullCourseId;
    private String title;
    private Integer favoriteCount;
    private Integer scrapCount;
    private Long memberId;
}
