package com.travelock.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyBlockConnectDTO {
    private Long dailyBlockConnectId;
    private Long fullBlockId; // 완성 블럭 ID
    private Integer blockNum; // 완성 블럭 순서
    private Long bigBlockId; // 빅블럭 ID
    private Long middleBlockId; // 미들블럭 ID
    private Long smallBlockId; // 스몰블럭 ID
}
