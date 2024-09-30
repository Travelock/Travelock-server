package com.travelock.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallBlockReviewDto {
    private Long smallBlockReviewId;
    private Long memberId;
    private String nickName;
    private Long smallBlockId;
    private String review;
}
