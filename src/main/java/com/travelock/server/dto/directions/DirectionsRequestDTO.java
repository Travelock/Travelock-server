package com.travelock.server.dto.directions;

import lombok.Data;

@Data
public class DirectionsRequestDTO {
    private String mapX;  // 블록 좌표(경도)
    private String mapY;  // 블록 좌표(위도)
}
