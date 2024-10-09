package com.travelock.server.controller;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.dto.BigBlockResponseDTO;
import com.travelock.server.dto.StateResponseDTO;
import com.travelock.server.service.BigBlockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/big")
@RequiredArgsConstructor
@Slf4j
public class BigBlockController {
    private final BigBlockService bigBlockService;

    // 전체 시/도 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<StateResponseDTO>> getAllStates() {
        List<StateResponseDTO> states = bigBlockService.getAllStates();
        return ResponseEntity.ok(states);
    }

    // 특정 시/도의 시/군/구(BigBlock) 목록 조회
    @GetMapping("/{stateCode}/cities")
    public ResponseEntity<List<BigBlockResponseDTO>> getCitiesByState(@PathVariable String stateCode) {
        List<BigBlockResponseDTO> cities = bigBlockService.getCitiesByState(stateCode);
        return ResponseEntity.ok(cities);
    }

    // 특정 시/도에 속한 시/군/구(BigBlock) 조회
    @GetMapping("/{stateCode}/{cityCode}")
    public ResponseEntity<BigBlockResponseDTO> getBigBlock(@PathVariable String stateCode, @PathVariable String cityCode) {
        BigBlockResponseDTO responseDTO = bigBlockService.getBigBlock(stateCode, cityCode);
        return ResponseEntity.ok(responseDTO);
    }

}



