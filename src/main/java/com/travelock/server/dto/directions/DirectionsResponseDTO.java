package com.travelock.server.dto.directions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectionsResponseDTO {
    private String mode; // WALK(도보)|BUS(버스)|SUBWAY(지하철)|EXPRESSBUS(고속/시외버스)|TRAIN(기차)|AIRPLANE(항공)|FERRY(해운)
    private List<PathDTO> path; // 모드별 경로 리스트
}
