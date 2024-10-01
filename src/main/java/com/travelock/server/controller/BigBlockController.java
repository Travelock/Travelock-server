package com.travelock.server.controller;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.service.BigBlockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/big")
@RequiredArgsConstructor
@Slf4j
public class BigBlockController {
    private final BigBlockService bigBlockService;


    // 기존 빅블럭 생성+조회 메서드 삭제


    // 특정 시/도에 속한 시/군/구(BigBlock) 조회 메서드
    @GetMapping("/{stateCode}/{cityCode}")
    public BigBlock getBigBlock(@PathVariable String stateCode, @PathVariable String cityCode) {
        return bigBlockService.getBigBlock(stateCode, cityCode);
    }
}



