package com.travelock.server.controller;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.dto.BigBlockRequest;
import com.travelock.server.service.BigBlockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/api/bigblock")
@RequiredArgsConstructor
@Slf4j
public class BigBlockController {
    private final BigBlockService bigBlockService;

    @PostMapping
    public BigBlock createBigBlock(@RequestBody BigBlockRequest request) {
        log.info("createBigBlock 호출, stateCode: {}, cityCode: {}", request.getStatecode(), request.getCityCode());
        return bigBlockService.createBigBlock(request.getStatecode(), request.getCityCode());
    }
}
