package com.travelock.server.controller;

import com.travelock.server.dto.block.PlaceSummaryResponseDTO;
import com.travelock.server.service.PlaceSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/summary")
public class PlaceSummaryController {

    private final PlaceSummaryService placeSummaryService;

    @Operation(summary = "도시 요약 정보 조회",
            tags = {"Place Summary API"},
            description = "지정된 도시 이름으로 위키피디아에서 도시 요약 정보를 가져옵니다.",
            parameters = {
                    @Parameter(name = "cityName", description = "조회할 도시 이름", required = true, in = ParameterIn.QUERY),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/info")
    public ResponseEntity<PlaceSummaryResponseDTO> getCitySummary(@RequestParam String cityName) {
        PlaceSummaryResponseDTO citySummary = placeSummaryService.getCitySummary(cityName);
        return ResponseEntity.ok(citySummary);
    }
}
