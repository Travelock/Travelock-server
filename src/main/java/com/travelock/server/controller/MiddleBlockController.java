package com.travelock.server.controller;

import com.travelock.server.domain.MiddleBlock;
import com.travelock.server.dto.MiddleBlockDTO;
import com.travelock.server.service.MiddleBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "전체 카테고리 조회",
            tags = {"미들블록 API - V1"},
            description = "모든 미들블록 카테고리를 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            })    @GetMapping("/categories")
    public ResponseEntity<List<MiddleBlockDTO>> getAllCategories() {
        List<MiddleBlockDTO> categories = middleBlockService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "카테고리 코드로 조회",
            tags = {"미들블록 API - V1"},
            description = "카테고리 코드를 사용하여 특정 미들블록 카테고리를 조회합니다.",
            parameters = {
                    @Parameter(name = "categoryCode", description = "카테고리 코드", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            })    @GetMapping("/category/code/{categoryCode}")
    public ResponseEntity<MiddleBlockDTO> getCategoryByCode(@PathVariable String categoryCode) {
        MiddleBlockDTO category = middleBlockService.getCategoryByCode(categoryCode);
        return ResponseEntity.ok(category);
    }

}
