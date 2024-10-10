package com.travelock.server.dto.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiddleBlockDTO {
    private Long middleBlockId; // 미들블록 ID
    private String categoryCode; // 카테고리 코드
    private String categoryName; // 카테고리 네임
}
