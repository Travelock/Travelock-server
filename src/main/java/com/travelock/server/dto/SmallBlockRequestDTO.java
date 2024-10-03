package com.travelock.server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallBlockRequestDTO {
    private String placeId; // 장소 ID
    private String placeName; // 장소 이름
    private String mapX;      // 지도 X 좌표
    private String mapY;      // 지도 Y 좌표
    private String url;       // 장소 URL
}
