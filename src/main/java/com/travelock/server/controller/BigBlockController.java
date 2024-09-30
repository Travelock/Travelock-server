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


    // 빅은 그냥 로컬에 박고 쓸거임.
    @PostMapping("/api/bigblock")
    public BigBlock createOrGetBigBlock(@RequestBody BigBlockRequest request) {
        log.info("createBigBlock 호출, stateCode: {}, cityCode: {}", request.getStatecode(), request.getCityCode());
        return bigBlockService.createOrGetBigBlock(request.getStatecode(), request.getCityCode());
    }
}


 // 미들은 사용자도 추가해야한다.
// 미들은 사용자가 추가해야 하는거.
// 미들에서 '서순라길' 추가하면 스몰에 서순라길에 있는거 뿌려지는거 예를들어 짹쨱
// 검색해서 나온것만.  API "서순라길" -> 검색되는 것만


// 스몰도 겟+ 크리에이트블록