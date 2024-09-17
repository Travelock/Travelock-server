package com.travelock.server.dto;

import lombok.Data;

@Data
public class SmallBlockReviewDto {
    private Long smallBlockReviewId;
    private Long memberId;
    private String nickName;
    private Long smallBlockId;
    private String review;
}
