package com.travelock.server.controller;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.State;
import com.travelock.server.dto.BigBlockResponseDTO;
import com.travelock.server.dto.StateResponseDTO;
import com.travelock.server.service.StateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/state")
@RequiredArgsConstructor
@Slf4j
public class StateController {

    private final StateService stateService;

    // 메서드 흐름 :
    // 사용자가 프론트에서 전체 시/도 목록 조회, 선택 (getAllStates)
    // -> 그 시/도에 맞는 도시 리스트 노출 (getCitiesByStates)


    // 전체 시/도 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<StateResponseDTO>> getAllStates() {
        List<StateResponseDTO> states = stateService.getAllStates();
        return ResponseEntity.ok(states);
    }

    // 특정 시/도의 시/군/구(BigBlock) 목록 조회
    @GetMapping("/{stateCode}/cities")
    public ResponseEntity<List<BigBlockResponseDTO>> getCitiesByState(@PathVariable String stateCode) {
        List<BigBlockResponseDTO> cities = stateService.getCitiesByState(stateCode);
        return ResponseEntity.ok(cities);
    }

}
