package com.travelock.server.controller;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.State;
import com.travelock.server.service.StateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/states")
@RequiredArgsConstructor
@Slf4j
public class StateController {
    private StateService stateService;

    // 시,도 목록 조회
    @GetMapping
    public List<State> getAllStates() {
        log.info("getAllStates 호출");
        return stateService.getAllStates();

    }

    // 시,구 목록 조회
    @GetMapping("/{stateCode}/cities")
    public List<BigBlock> getCitiesByState(@PathVariable String stateCode) {
        log.info("getCitiesByState 호출, stateCode: {}", stateCode);
        return stateService.getCitiesByState(stateCode);
    }
}
