package com.travelock.server.dto.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BigBlockResponseDTO {
    private Long bigBlockId;
    private Long stateId;
    private String cityCode;
    private String cityName;
    private String stateName;
}
