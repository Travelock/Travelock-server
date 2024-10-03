package com.travelock.server.dto.tmap;

import lombok.Data;

import java.util.List;

@Data
public class PlanDTO {
    private List<ItineraryDTO> itineraries;
}
