package com.travelock.server.dto.tmap;

import lombok.Data;

@Data
public class StepDTO {
    private int distance; // 도보 이동거리
    private String streetName; // 도로명
    private String description; // 도보 구간 정보 (수유역  8번출구 에서 우회전 후 도봉로 을 따라 15m 이동 )
    private String linestring; // 도조 구간 좌표 (127.02597,37.63855 127.02606,37.63864 127.02608,37.638657)
}