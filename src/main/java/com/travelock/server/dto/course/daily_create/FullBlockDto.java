package com.travelock.server.dto.course.daily_create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullBlockDto {
    private Integer blockNum;
    private Long bigBlockId;
    private Long middleBlockId;
    private SmallBlockDto smallBlockDto;
}
