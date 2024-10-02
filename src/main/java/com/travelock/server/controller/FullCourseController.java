package com.travelock.server.controller;

import com.travelock.server.converter.DTOConverter;
import com.travelock.server.domain.FullCourse;
import com.travelock.server.dto.DailyCourseRequestDTO;
import com.travelock.server.dto.FullCourseRequestDTO;
import com.travelock.server.dto.FullCourseResponseDTO;
import com.travelock.server.dto.SmallBlockReviewDto;
import com.travelock.server.dto.course.daily_create.DailyCourseCreateDto;
import com.travelock.server.dto.course.full_create.FullCourseCreateDto;
import com.travelock.server.service.FullCourseService;
import com.travelock.server.service.cache.CourseRecommendService;
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

import java.util.List;

@RestController
@RequestMapping("/api/course/full")
@RequiredArgsConstructor
@Slf4j
public class FullCourseController {
    private final FullCourseService fullCourseService;
    private final CourseRecommendService courseRecommendService;

    @Operation(summary = "사용자의 전체일정 조회",
            tags = {"전체일정 API - V1"},
            description = "사용자의 전체일정 조회",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID", required = true, in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(
                            schema = @Schema(implementation = FullCourseResponseDTO.class),
                            mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getFullCoursesByMember(@PathVariable Long memberId) {
        // Response DTO로 변환해서 반환
        List<FullCourseResponseDTO> response = DTOConverter.toDtoList(fullCourseService.findMemberFullCourses(memberId)
                , FullCourseResponseDTO::fromDomainToResponseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "전체일정 저장",
            tags = {"전체일정 API - V1"},
            description = "전체일정 저장",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "전체일정 요청 Dto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FullCourseRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "전체일정 저장 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "전체일정 저장 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @PostMapping
    public ResponseEntity<?> createFullCourse(@RequestBody FullCourseRequestDTO request) {
        // Response DTO로 변환해서 반환
        FullCourseResponseDTO response = DTOConverter.toDto(fullCourseService.saveFullCourse(request)
                , FullCourseResponseDTO::fromDomainToResponseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


//    //일정 교체
//    public ResponseEntity<?> changeDailyCourse(@RequestBody DailyCourseRequestDTO requestDTO){
//        fullCourseService.changeDailyCourse(requestDTO);
//    }


    @Operation(summary = "추천 전체일정",
            tags = {"전체일정 API - V1"},
            description = "캐시된 추천 전체일정 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "조회 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/recommend")
    public ResponseEntity<?> getRecommendedFullCourses(){
        List<FullCourse> topFullCoursesFromCache = courseRecommendService.getTopFullCoursesFromCache();
        return ResponseEntity.status(HttpStatus.OK).body(topFullCoursesFromCache);
    }



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
