package com.travelock.server.controller;

import com.travelock.server.domain.FullCourseScrap;
import com.travelock.server.service.FullCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course/full")
@RequiredArgsConstructor
@Slf4j
public class FullCourseController {
    private final FullCourseService fullCourseService;



    @Operation(summary = "전체일정 좋아요",
            tags = {"전체일정 API - V1"},
            description = "전체일정 좋아요",
            parameters = {
                    @Parameter(name = "fullCourseId", description = "전체일정 ID", required = true, in = ParameterIn.PATH),
                    @Parameter(name = "memberId", description = "사용자 ID", required = true, in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "좋아요 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "좋아요 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @PostMapping("/favorite/{fullCourseId}")
    public ResponseEntity<?> setFullCourseFavorite(@PathVariable Long fullCourseId, @RequestParam Long memberId){
        fullCourseService.setFavorite(fullCourseId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("좋아요 성공");
    }


    @Operation(summary = "전체일정 스크랩",
            tags = {"전체일정 API - V1"},
            description = "전체일정 스크랩",
            parameters = {
                    @Parameter(name = "fullCourseId", description = "전체일정 ID", required = true, in = ParameterIn.PATH),
                    @Parameter(name = "memberId", description = "사용자 ID", required = true, in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "스크랩 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "스크랩 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @PostMapping("/scrap/{fullCourseId}")
    public ResponseEntity<?> setFullCourseScrap(@PathVariable Long fullCourseId, @RequestParam Long memberId){
        fullCourseService.setScrap(fullCourseId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("스크랩 성공");

    }

}
