package com.travelock.server.service;

import com.travelock.server.client.DirectionsSearchClient;
import com.travelock.server.dto.DirectionsRequestDTO;
import com.travelock.server.dto.DirectionsResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectionsService {
    private final DirectionsSearchClient directionsSearchClient;

    public DirectionsResponseDTO searchDirections(DirectionsRequestDTO requestDTO) {
        // @TODO 경로 찾기 비즈니스 로직
        return directionsSearchClient.requestSearchDirections(requestDTO);
                //.thenApply(directionsResponseDTO -> directionsResponseDTO)
                //.exceptionally(error -> {
                //    log.error("DirectionsService::searchDirections ERROR : " + error.getMessage());
                //    throw new RuntimeException("Error fetching directions", error);
                //});
    }
}
