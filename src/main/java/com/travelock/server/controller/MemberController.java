package com.travelock.server.controller;


import com.travelock.server.domain.DailyCourseScrap;
import com.travelock.server.domain.FullCourseScrap;
import com.travelock.server.dto.SmallBlockReviewDto;
import com.travelock.server.service.DailyCourseService;
import com.travelock.server.service.FullCourseService;
import com.travelock.server.service.MemberService;
import com.travelock.server.service.SmallBlockReviewService;
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
@RequestMapping("/api/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final FullCourseService fullCourseService;
    private final DailyCourseService dailyCourseService;
    private final SmallBlockReviewService smallBlockReviewService;
    private final MemberService memberService;


    @Operation(summary = "회원 탈퇴",
            tags = {"사용자 API - V1"},
            description = "회원 탈퇴. 활성화 상태 Off, 랜덤데이터로 대체",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "탈퇴 완료", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "탈퇴 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> leave(@PathVariable Long memberId){
        memberService.leave(memberId);
        return ResponseEntity.status(HttpStatus.OK).body("또 만나요");
    }

//    사용안하기로함
//
//    @GetMapping("/course/full/favorites/{memberId}")
//    public ResponseEntity<?> getMyFullCourseFavorites(@PathVariable Long memberId){
//        List<FullCourseFavorite> myFavorites = fullCourseService.getMyFavorites(memberId);
//        return ResponseEntity.status(HttpStatus.OK).body(myFavorites);
//
//    }
//    @GetMapping("/course/daily/favorites/{memberId}")
//    public ResponseEntity<?> getMyDailyCourseFavorites(@PathVariable Long memberId){
//        List<DailyCourseFavorite> myFavorites = dailyCourseService.getMyFavorites(memberId);
//        return ResponseEntity.status(HttpStatus.OK).body(myFavorites);
//    }

    @Operation(summary = "사용자의 전체일정 스크랩 조회",
            tags = {"사용자 API - V1"},
            description = "사용자의 전체일정 스크랩 조회",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 조회", content = @Content(
                            schema = @Schema(implementation = FullCourseScrap.class),
                            mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "정보 조회 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/course/full/scraps/{memberId}")
    public ResponseEntity<?> getMyFullCourseScraps(@PathVariable Long memberId){
        List<FullCourseScrap> myScraps = fullCourseService.getMyScraps(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(myScraps);
    }

    @Operation(summary = "사용자의 일일일정 스크랩 조회",
            tags = {"사용자 API - V1"},
            description = "사용자의 일일일정 스크랩 조회",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 조회", content = @Content(
                            schema = @Schema(implementation = DailyCourseScrap.class),
                            mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "정보 조회 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/course/daily/scraps/{memberId}")
    public ResponseEntity<?> getMyDailyCourseScraps(@PathVariable Long memberId){
        List<DailyCourseScrap> myScraps = dailyCourseService.getMyScraps(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(myScraps);

    }


    @Operation(summary = "사용자의 스몰블럭 리뷰 조회",
            tags = {"사용자 API - V1"},
            description = "사용자의 스몰블럭 리뷰 조회",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 조회", content = @Content(
                            schema = @Schema(implementation = SmallBlockReviewDto.class),
                            mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "정보 조회 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/block/small/review/{memberId}")
    public ResponseEntity<?> getMySmallBlockReviews(@PathVariable Long memberId){
        List<SmallBlockReviewDto> reviews = smallBlockReviewService.getMyReviews(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }
}
