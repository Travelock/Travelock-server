package com.travelock.server.controller;

import com.travelock.server.converter.DTOConverter;
import com.travelock.server.domain.DailyCourse;
import com.travelock.server.dto.DailyCourseRequestDTO;
import com.travelock.server.dto.DailyCourseResponseDTO;
import com.travelock.server.dto.course.daily_create.DailyCourseCreateDto;
import com.travelock.server.service.DailyCourseService;
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
@RequestMapping("/api/course/daily")
@RequiredArgsConstructor
@Slf4j
public class DailyCourseController {
   private final DailyCourseService dailyCourseService;
//   private final CourseRecommendService courseRecommendService;

    @Operation(summary = "일일일정 조회",
            tags = {"일일일정 API - V1"},
            description = "일일일정 조회",
            parameters = {
                    @Parameter(name = "dailyCourseId", description = "일일일정 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/{dailyCourseId}")
    public ResponseEntity<?> getDailyCourseById(@PathVariable Long dailyCourseId) {
        return ResponseEntity.ok(DTOConverter.toDto(dailyCourseService.findDailyCourse(dailyCourseId)
                ,DailyCourseResponseDTO::fromDomainToResponseDTO));
    }

    @Operation(summary = "일일일정 저장",
            tags = {"일일일정 API - V1"},
            description = "일일일정 저장",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "일일일정 생성 Dto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DailyCourseCreateDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "일일일정 저장 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "일일일정 저장 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @PostMapping
    public ResponseEntity<?> createDailyCourse(@RequestBody DailyCourseCreateDto request) {
        // Response DTO로 변환해서 반환
        DailyCourseResponseDTO response = DTOConverter.toDto(dailyCourseService.saveDailyCourse(request),
                DailyCourseResponseDTO::fromDomainToResponseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "일일일정 수정",
            tags = {"일일일정 API - V1"},
            description = "일일일정 수정",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "일일일정 생성 Dto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DailyCourseCreateDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "일일일정 저장 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "일일일정 저장 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @PutMapping
    public ResponseEntity<?> modifyDailyCourse(@RequestBody DailyCourseCreateDto request){

        // Response DTO로 변환해서 반환
        DailyCourseResponseDTO response = DTOConverter.toDto(dailyCourseService.modifyDailyCourse(request)
                , DailyCourseResponseDTO::fromDomainToResponseDTO);
        return ResponseEntity.status(HttpStatus.OK).body("수정됨");
    }


//    //일일일정 조회
//   조회 public ResponseEntity<?> getDailyCourse(){getDailyCourse
//        dailyCourseService
//    }

//    //내 모든 일일일정 조회
//    public ResponseEntity<?> getMyDailyCourses(){}




//    @Operation(summary = "추천 일일일정",
//            tags = {"일일일정 API - V1"},
//            description = "캐시된 추천 일일일정 조회",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
//                    @ApiResponse(responseCode = "404", description = "조회 실패", content = @Content(mediaType = "application/json")),
//                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
//            })
//    @GetMapping("/recommend")
//    public ResponseEntity<?> getRecommendedDailyCourses(){
//        List<DailyCourse> topDailyCoursesFromCache = courseRecommendService.getTopDailyCoursesFromCache();
//        return ResponseEntity.status(HttpStatus.OK).body(topDailyCoursesFromCache);
//    }


   @Operation(summary = "일일일정 좋아요",
           tags = {"일일일정 API - V1"},
           description = "일일일정 좋아요",
           parameters = {
                   @Parameter(name = "dailyCourseId", description = "일일일정 ID", required = true, in = ParameterIn.PATH),
                   @Parameter(name = "memberId", description = "사용자 ID", required = true, in = ParameterIn.QUERY)
           },
           responses = {
                   @ApiResponse(responseCode = "201", description = "좋아요 성공", content = @Content(mediaType = "application/json")),
                   @ApiResponse(responseCode = "400", description = "좋아요 실패", content = @Content(mediaType = "application/json")),
                   @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
           })
   @PostMapping("/favorite/{dailyCourseId}")
   public ResponseEntity<?> setDailyCourseFavorite(@PathVariable Long dailyCourseId, @RequestParam Long memberId){
       dailyCourseService.setFavorite(dailyCourseId, memberId);
       return ResponseEntity.status(HttpStatus.CREATED).body("좋아요 성공");
   }

   @Operation(summary = "일일일정 스크랩",
           tags = {"일일일정 API - V1"},
           description = "일일일정 스크랩",
           parameters = {
                   @Parameter(name = "dailyCourseId", description = "일일일정 ID", required = true, in = ParameterIn.PATH),
                   @Parameter(name = "memberId", description = "사용자 ID", required = true, in = ParameterIn.QUERY)
           },
           responses = {
                   @ApiResponse(responseCode = "201", description = "스크랩 성공", content = @Content(mediaType = "application/json")),
                   @ApiResponse(responseCode = "400", description = "스크랩 실패", content = @Content(mediaType = "application/json")),
                   @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
           })
   @PostMapping("/scrap/{dailyCourseId}")
   public ResponseEntity<?> setDailyCourseScrap(@PathVariable Long dailyCourseId, @RequestParam Long memberId){
       dailyCourseService.setScrap(dailyCourseId, memberId);
       return ResponseEntity.status(HttpStatus.CREATED).body("스크랩 성공");
   }

}
