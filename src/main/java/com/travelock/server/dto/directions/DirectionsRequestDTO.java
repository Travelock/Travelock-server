package com.travelock.server.dto.directions;

import lombok.Data;

@Data
public class DirectionsRequestDTO {
    // 대중교통 길찾기 Request DTO
    private String startX;  // 출발지 좌표(경도)
    private String startY;  // 출발지 좌표(위도)
    private String endX;    // 도착지 좌표(경도)
    private String endY;    // 도착지 좌표(위도)

    // Option
    private String lang;    // 언어 선택 (0(default): 국문, 1: 영문)
    private String count;   // 최대 응답 개수 (최대 10)
    private String searchDttm;
}
