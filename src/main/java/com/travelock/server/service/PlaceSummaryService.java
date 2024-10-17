package com.travelock.server.service;

import com.travelock.server.client.PlaceSummaryClient;
import com.travelock.server.dto.block.PlaceSummaryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceSummaryService {

    private final PlaceSummaryClient placeSummaryClient;

    public PlaceSummaryResponseDTO getCitySummary(String cityName) {
        return placeSummaryClient.fetchCitySummary(cityName);
    }
}
