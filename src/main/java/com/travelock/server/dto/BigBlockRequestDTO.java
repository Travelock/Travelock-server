package com.travelock.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BigBlockRequestDTO {
    private String cityCode;
    private String cityName;
    private Long stateId;
}
