package com.travelock.server.dto.tmap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.travelock.server.dto.tmap.PlanDTO;
import com.travelock.server.dto.tmap.RequestParametersDTO;
import lombok.Data;

@Data
public class MetaDataDTO {
    // TMAP 대중교통 길찾기 Response DTO
    private RequestParametersDTO requestParameters;
    private PlanDTO plan;
}
