package com.travelock.server.dto.tmap;

import lombok.Data;

import java.util.List;

@Data
public class PassStopListDTO {
    private List<StationDTO> stations; // 정류장 상세정보
}
