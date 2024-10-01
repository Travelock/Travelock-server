package com.travelock.server.controller;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.State;
import com.travelock.server.service.StateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    // 시/도 목록 조회하는 메서드
    @GetMapping("/list")
    public List<State> getAllStates() {
        log.info("모든 시/도 목록 조회 호출 - ");
        return stateService.getAllStates();
    }

    // 특정 시/도의 시/군/구 조회하는 메서드
    @GetMapping("/{stateCode}/cities")
    public List<BigBlock> getCitiesByState(@PathVariable String stateCode) {
        return stateService.getCitiesByState(stateCode);
    }

}
