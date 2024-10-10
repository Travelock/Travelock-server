package com.travelock.server.dto.directions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectionsResponseDTO {
    private List<List<RouteDTO>> routes = new ArrayList<>();
}
