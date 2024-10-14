package com.travelock.server.controller;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.dto.BigBlockResponseDTO;
import com.travelock.server.dto.StateResponseDTO;
import com.travelock.server.dto.course.daily.DailyCourseRequestDTO;
import com.travelock.server.service.BigBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/big")
@RequiredArgsConstructor
@Slf4j
public class BigBlockController {
    private final BigBlockService bigBlockService;

    @Operation(summary = "도/광역시/특별시 목록 조회",
            tags = {"빅블록 API - V1"},
            description = "모든 도/광역시/특별시 목록을 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            })
    @GetMapping("/list")
    public ResponseEntity<List<StateResponseDTO>> getAllStates() {
        List<StateResponseDTO> states = bigBlockService.getAllStates();
        return ResponseEntity.ok(states);
    }


    @Operation(summary = "특정 시/도의 시/군/구 목록 조회",
            tags = {"빅블록 API - V1"},
            description = "특정 시/도의 시/군/구 목록을 조회.",
            parameters = {
                    @Parameter(name = "stateCode", description = "도/광역시/특별시 코드", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            })
    @GetMapping("/{stateCode}/cities")
    public ResponseEntity<List<BigBlockResponseDTO>> getCitiesByState(@PathVariable String stateCode) {
        List<BigBlockResponseDTO> cities = bigBlockService.getCitiesByState(stateCode);
        return ResponseEntity.ok(cities);
    }


    @Operation(summary = "특정 시/군/구 조회",
            tags = {"빅블록 API - V1"},
            description = "특정 시/군/구(BigBlock)를 조회.",
            parameters = {
                    @Parameter(name = "stateCode", description = "도/광역시/특별시 코드", required = true, in = ParameterIn.PATH),
                    @Parameter(name = "cityCode", description = "시/군/구 코드", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "해당 시/군/구를 찾을 수 없음", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            })
    @GetMapping("/{stateCode}/{cityCode}")
    public ResponseEntity<BigBlockResponseDTO> getBigBlock(@PathVariable String stateCode, @PathVariable String cityCode) {
        BigBlockResponseDTO responseDTO = bigBlockService.getBigBlock(stateCode, cityCode);
        return ResponseEntity.ok(responseDTO);
    }


    //메인화면에서 사용
    @GetMapping
    public ResponseEntity<?> getAllCities(){
        List<BigBlockResponseDTO> allCities = bigBlockService.getAllCities();
        return ResponseEntity.status(HttpStatus.OK).body(allCities);
    }
}



