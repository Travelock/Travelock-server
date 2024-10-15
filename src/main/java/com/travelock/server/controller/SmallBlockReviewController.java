package com.travelock.server.controller;

import com.travelock.server.dto.block.SmallBlockReviewDto;
import com.travelock.server.service.SmallBlockReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/block/small/review")
@RequiredArgsConstructor
public class SmallBlockReviewController {
    private final SmallBlockReviewService smallBlockReviewService;

    @Operation(summary = "스몰블록의 모든 리뷰 조회",
            tags = {"스몰블록 리뷰 API - V1"},
            description = "스몰블록의 모든 리뷰 조회",
            parameters = {
                    @Parameter(name = "smallBlockId", description = "조회할 리뷰의 스몰블록 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 조회", content = @Content(
                            schema = @Schema(implementation = SmallBlockReviewDto.class),
                            mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "정보 조회 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/all/{smallBlockId}")
    public ResponseEntity<?> getAllReviews(@PathVariable Long smallBlockId){
        List<SmallBlockReviewDto> reviews = smallBlockReviewService.getAllReviews(smallBlockId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @Operation(summary = "스몰블록의 특정 리뷰 조회",
            tags = {"스몰블록 리뷰 API - V1"},
            description = "스몰블록의 특정 리뷰 조회",
            parameters = {
                    @Parameter(name = "smallBlockReviewId", description = "조회할 스몰블록 리뷰의 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 조회", content = @Content(
                            schema = @Schema(implementation = SmallBlockReviewDto.class),
                            mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "정보 조회 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/{smallBlockReviewId}")
    public ResponseEntity<?> getReview(@PathVariable Long smallBlockReviewId){
        SmallBlockReviewDto reviews = smallBlockReviewService.getReview(smallBlockReviewId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }


    @Operation(summary = "스몰블록 리뷰 추가",
            tags = {"스몰블록 리뷰 API - V1"},
            description = "스몰블록 리뷰 추가",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "스몰블록 리뷰 추가",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SmallBlockReviewDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "리뷰 작성 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "리뷰 작성 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody SmallBlockReviewDto smallBlockReviewDto){
        smallBlockReviewService.addReview(
                smallBlockReviewDto.getMemberId(),
                smallBlockReviewDto.getSmallBlockId(),
                smallBlockReviewDto.getReview());
        return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 작성 성공");
    }


    @Operation(summary = "스몰블록 리뷰 수정",
            tags = {"스몰블록 리뷰 API - V1"},
            description = "스몰블록 리뷰 수정",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "스몰블록 리뷰 수정",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SmallBlockReviewDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "리뷰 수정 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "리뷰 수정 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @PutMapping
    public ResponseEntity<?> modifyReview(@RequestBody SmallBlockReviewDto smallBlockReviewDto){
        smallBlockReviewService.modifyReview(
                smallBlockReviewDto.getSmallBlockReviewId(),
                smallBlockReviewDto.getReview());
        return ResponseEntity.status(HttpStatus.OK).body("리뷰 수정 성공");
    }


    @Operation(summary = "스몰블록 리뷰 삭제",
            tags = {"스몰블록 리뷰 API - V1"},
            description = "스몰블록 리뷰 삭제",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "스몰블록 리뷰 삭제",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SmallBlockReviewDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "리뷰 삭제 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @DeleteMapping
    public ResponseEntity<?> removeReview(@PathVariable Long smallBlockReviewId){
        smallBlockReviewService.removeReview(smallBlockReviewId);
        return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제 성공");
    }
}
