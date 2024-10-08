package com.travelock.server.service;

import com.travelock.server.client.DirectionsSearchClient;
import com.travelock.server.dto.directions.DirectionsRequestDTO;
import com.travelock.server.dto.directions.DirectionsResponseDTO;
import com.travelock.server.dto.directions.PathDTO;
import com.travelock.server.dto.tmap.LegDTO;
import com.travelock.server.dto.tmap.StationDTO;
import com.travelock.server.dto.tmap.StepDTO;
import com.travelock.server.dto.tmap.TmapResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectionsService {
    private final DirectionsSearchClient directionsSearchClient;

    public List<DirectionsResponseDTO> searchDirections(DirectionsRequestDTO requestDTO) {
        TmapResponseDTO tmapResponseDTO = directionsSearchClient.requestSearchDirections(requestDTO);
        List<DirectionsResponseDTO> directionsList = new ArrayList<>();

        // Tmap 응답 결과 > DirectionsResponseDTO로 변환
        List<LegDTO> responseList = tmapResponseDTO.getMetaData().getPlan().getItineraries().get(0).getLegs(); // 우선 첫번째 경로 정보 가져오기
        for (LegDTO response : responseList) {
            String mode = response.getMode();

            List<PathDTO> pathList = new ArrayList<>(); // mode별 경로 정보 리스트
            // WALK : steps
            if (mode.equals("WALK") && response.getSteps() != null && !response.getSteps().isEmpty()) {
                for (StepDTO step : response.getSteps()) {
                    // "127.02449,37.504467 127.024666,37.50452"
                    String[] lines = step.getLinestring().split(" ");
                    for (String line : lines) {
                        String[] point = line.split(",");
                        pathList.add(new PathDTO(point[0], point[1]));
                    }
                }
            }
            // SUBWAY, BUS: stationList
            if ((mode.equals("SUBWAY") || mode.equals("BUS"))
                    && response.getPassStopList() != null
                    && response.getPassStopList().getStationList() != null && !response.getPassStopList().getStationList().isEmpty()) {
                for (StationDTO station : response.getPassStopList().getStationList()) {
                    pathList.add(new PathDTO(station.getLon(), station.getLat()));
                }
            }

            directionsList.add(new DirectionsResponseDTO(
                    response.getMode(),
                    pathList
            ));
        }

        return directionsList;
                //.thenApply(directionsResponseDTO -> directionsResponseDTO)
                //.exceptionally(error -> {
                //    log.error("DirectionsService::searchDirections ERROR : " + error.getMessage());
                //    throw new RuntimeException("Error fetching directions", error);
                //});
    }
}
