package com.travelock.server.dto.block;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallBlockRequestDTO {
    private Long smallBlockId; // 필수 X | 이미 저장된 SmallBlock인 경우, Front에서 요청시 보내옴
    private String mapX;
    private String mapY;
    private String placeId;
    private String placeName;
    private Long bigBlockId;
}



