package com.travelock.server.controller;

import com.travelock.server.domain.MiddleBlock;
import com.travelock.server.dto.MiddleBlockDTO;
import com.travelock.server.service.MiddleBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/middle")
@RequiredArgsConstructor
public class MiddleBlockController {

    private final MiddleBlockService middleBlockService;

    // 전체 카테고리 조회
    @GetMapping("/categories")
    public ResponseEntity<List<MiddleBlockDTO>> getAllCategories() {
        List<MiddleBlockDTO> categories = middleBlockService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // 특정 카테고리 조회
    @GetMapping("/category/{categoryCode}")
    public ResponseEntity<MiddleBlockDTO> getCategoryByCode(@PathVariable String categoryCode) {
        MiddleBlockDTO category = middleBlockService.getCategoryByCode(categoryCode);
        return ResponseEntity.ok(category);
    }
}
