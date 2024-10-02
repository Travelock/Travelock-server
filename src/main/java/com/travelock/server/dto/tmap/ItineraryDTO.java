package com.travelock.server.dto.tmap;

import lombok.Data;

import java.util.List;

@Data
public class ItineraryDTO {
    private int totalTime;          // 총 소요시간
    private int totalDistance;      // 총 이동거리(m)
    private int totalWalkTime;      // 총 보행자 소요시간(sec)
    private int totalWalkDistance;  // 총 보행자 이동거리(m)
    private int transferCount;      // 환승 횟수
    private int pathType;           // 경로 탐색 결과 종류(1:지하철, 2:버스, 3:버스+지하철, 4:고속/시외버스, 5:기차, 6:항공, 7:해운)
    private FareDTO fare;
    private List<LegDTO> legs;
}
