package com.travelock.server.dto.tmap;

import lombok.Data;

import java.util.List;

@Data
public class LegDTO {
    private int distance;       // 구간별 이동거리(m)
    private int sectionTime;    // 구간별 소요시간(sec)
    private String mode;        // 이동수단 종류 WALK(도보)|BUS(버스)|SUBWAY(지하철)|EXPRESSBUS(고속/시외버스)|TRAIN(기차)|AIRPLANE(항공)|FERRY(해운)

    private String route;       // 대중교통 노선 명칭
    private String routeColor;  // 대중교통 노선 색상
    private String routeId;     // 대중교통 노선 ID

    private int type;           // 이동수단별 노선코드 (가이드 > 이동수단 코드 참조)
    private int routePayment;   // 광역이동수단 요금(고속/시외버스, 기차, 항공, 해운)
    private int service;        // 이동수단 운행여부 flag (0: 운행종료, 1: 운행중)

    private passShapeDTO passShape;         // 대중교통 구간 좌표 최상위 노드
    private PassStopListDTO passStopList;   // 대중교통 구간 정류장 정보 최상위 노드
    private List<StepDTO> steps;            // 구간별 도보상세정보 최상위 노드

    private StationDTO start;
    private StationDTO end;

}
