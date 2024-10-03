package com.travelock.server.controller;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.dto.BigBlockResponseDTO;
import com.travelock.server.service.BigBlockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/big")
@RequiredArgsConstructor
@Slf4j
public class BigBlockController {
    private final BigBlockService bigBlockService;

    // 특정 시/도에 속한 시/군/구(BigBlock) 조회 메서드
    @GetMapping("/{stateCode}/{cityCode}")
    public ResponseEntity<BigBlockResponseDTO> getBigBlock(@PathVariable String stateCode, @PathVariable String cityCode) {
        BigBlockResponseDTO responseDTO = bigBlockService.getBigBlock(stateCode, cityCode);
        return ResponseEntity.ok(responseDTO);  // ResponseEntity로 DTO 반환
    }

//    @GetMapping("/{stateCode}/{cityCode}")
//    public ResponseEntity<?> getBigBlock(@PathVariable String stateCode, @PathVariable String cityCode) {
//        log.info("API 호출: stateCode={}, cityCode={}", stateCode, cityCode);
//        return bigBlockService.getBigBlock(stateCode, cityCode);
//    }
}



