package com.travelock.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PathDTO {
    private String mapX; // 경도(-180~180)
    private String mapY; // 위도(-90~90)
}
