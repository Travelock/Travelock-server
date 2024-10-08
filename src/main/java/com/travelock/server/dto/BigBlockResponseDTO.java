package com.travelock.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BigBlockResponseDTO {
    private Long bigBlockId;
    private String cityCode;
    private String cityName;
    private String stateName;
}
