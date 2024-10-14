package com.travelock.server.dto.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateResponseDTO {
    private Long stateId;
    private String stateCode;
    private String stateName;
}
