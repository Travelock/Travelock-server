package com.travelock.server.dto.tmap;

import lombok.Data;

@Data
public class TmapRequestDTO {
    // 대중교통 길찾기 Request DTO
    private String startX;  // 출발지 좌표(경도)
    private String startY;  // 출발지 좌표(위도)
    private String endX;    // 도착지 좌표(경도)
    private String endY;    // 도착지 좌표(위도)

    // Option
    private String lang;    // 언어 선택 (0(default): 국문, 1: 영문)
    private String count;   // 최대 응답 개수 (최대 10)
    private String searchDttm;

    public TmapRequestDTO(String startX, String startY, String endX, String endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
}
