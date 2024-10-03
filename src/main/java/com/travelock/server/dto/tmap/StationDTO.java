package com.travelock.server.dto.tmap;

import lombok.Data;

@Data
public class StationDTO {
    private int index;          // 순번
    private String stationName; // 정류장 명칭 (stations > stationName)
    private String stationID;   // 정류장 ID

    private String lon;         // 정류장 좌표(경도)
    private String lat;         // 정류장 좌표(위도)

    private String name; // 출발/도착 정류장 명칭 (legs > start / end > name)
}
