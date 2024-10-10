package com.travelock.server.service;

import com.travelock.server.client.DirectionsSearchClient;
import com.travelock.server.dto.directions.DirectionsRequestDTO;
import com.travelock.server.dto.directions.DirectionsResponseDTO;
import com.travelock.server.dto.directions.PathDTO;
import com.travelock.server.dto.directions.RouteDTO;
import com.travelock.server.dto.tmap.*;
import com.travelock.server.exception.base_exceptions.BadRequestException;
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

    public DirectionsResponseDTO searchDirections(List<DirectionsRequestDTO> requestDTO) {
        DirectionsResponseDTO directionsResponse = new DirectionsResponseDTO();

        List<TmapRequestDTO> tmapRequestList = new ArrayList<>();
        for (int i = 0; i < requestDTO.size() - 1; i++) {
            // 필수 파라미터 검증
            if (requestDTO.get(i).getMapX() == null || requestDTO.get(i).getMapX().isBlank() || requestDTO.get(i).getMapY() == null || requestDTO.get(i).getMapY().isBlank()) {
                log.info("DirectionsService::searchDirections - Service Error : 길찾기 필수 파라미터 누락");
                throw new BadRequestException("[Service Error] 길찾기 필수 파라미터 누락");
            }
            // Tmap Request DTO
            tmapRequestList.add(
                    new TmapRequestDTO(
                        requestDTO.get(i).getMapX(),
                        requestDTO.get(i).getMapY(),
                        requestDTO.get(i+1).getMapX(), // 다음 블록이 도착 지점이 됨
                        requestDTO.get(i+1).getMapY()
                    )
            );
        }
        if (tmapRequestList.isEmpty()) throw new BadRequestException("대중교통 길찾기 필수 파라미터가 없습니다.");

        // 블록간 경로 찾기
        List<RouteDTO> routeList = new ArrayList<>();
        for (TmapRequestDTO tmapRequest : tmapRequestList) {
            // 필수 파라미터 검증
            if (tmapRequest.getStartX() == null || tmapRequest.getStartX().isBlank() || tmapRequest.getStartY() == null || tmapRequest.getStartY().isBlank()
                || tmapRequest.getEndX() == null || tmapRequest.getEndX().isBlank() || tmapRequest.getEndY() == null || tmapRequest.getEndY().isBlank()) {
                log.info("DirectionsService::searchDirections - Tmap Error : 길찾기 필수 파라미터 누락");
                throw new BadRequestException("[Tmap Error] 길찾기 필수 파라미터 누락");
            }

            // 경로별 응답 DTO 세팅
            TmapResponseDTO tmapResponseDTO = directionsSearchClient.requestSearchDirections(tmapRequest);
            log.info("tmapResponse: "+tmapResponseDTO);

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

                routeList.add(new RouteDTO(
                        response.getMode(),
                        pathList
                ));
            }
            // 블록간 경로 추가
            directionsResponse.getRoutes().add(routeList);
        }

        return directionsResponse;
                //.thenApply(directionsResponseDTO -> directionsResponseDTO)
                //.exceptionally(error -> {
                //    log.error("DirectionsService::searchDirections ERROR : " + error.getMessage());
                //    throw new RuntimeException("Error fetching directions", error);
                //});
    }
}
