package com.travelock.server.dto.tmap;

import lombok.Data;

@Data
public class RequestParametersDTO {
    private String reqDttm;         // 요청시간 (yyyymmddhhmiss)
    private String startX;          // 출발지 좌표(경도)
    private String startY;          // 출발지 좌표(위도)
    private String endX;            // 도착지 좌표(경도)
    private String endY;            // 도착지 좌표(위도)
    private String locale;          // 언어 코드 (ko|en)
    private int busCount;           // 버스 결과 개수
    private int expressbusCount;    // 고속/시외버스 결과 개수
    private int subwayCount;        // 지하철 결과 개수
    private int airplaneCount;      // 항공 결과 개수
    private int wideareaRouteCount; // 광역노선 결과 개수
    private int subwayBusCount;     // 버스+지하철 결과 개수
    private int ferryCount;         // 해운 결과 개수
    private int trainCount;         // 기차 결과 개수
}
