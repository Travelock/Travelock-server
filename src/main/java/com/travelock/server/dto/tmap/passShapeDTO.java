package com.travelock.server.dto.tmap;

import lombok.Data;

@Data
public class passShapeDTO {
    private String linestring; // 대중교통 구간 좌표 ("127.025428,37.504742 127.022200,37.503750 127.020583,...")
}
