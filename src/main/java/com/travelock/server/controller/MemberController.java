package com.travelock.server.controller;


import com.travelock.server.domain.DailyCourseScrap;
import com.travelock.server.domain.FullCourseScrap;
import com.travelock.server.domain.Member;
import com.travelock.server.dto.block.SmallBlockReviewDto;
import com.travelock.server.dto.course.daily.DailyCourseResponseDTO;
import com.travelock.server.dto.course.full.FullCourseResponseDTO;
import com.travelock.server.dto.oauth2DTO.MemberDTO;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final FullCourseService fullCourseService;
    private final DailyCourseService dailyCourseService;
    private final SmallBlockReviewService smallBlockReviewService;
    private final MemberService memberService;


    @GetMapping("/info") //사용자 정보 조회
    public ResponseEntity<?> getMemberInfo(){
        Member member = memberService.getInfo();

        System.out.println(member.getMemberId());

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(member.getMemberId());
        memberDTO.setNickname(member.getNickName());
        memberDTO.setEmail(memberDTO.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
    }

    @GetMapping("/course/full")
    public ResponseEntity<?> getMyFullCourse(){
        List<FullCourseResponseDTO> myScraps = fullCourseService.getMyScraps(1L);
        return ResponseEntity.status(HttpStatus.OK).body(myScraps);
    }

    @GetMapping("/course/daily")
    public ResponseEntity<?> getMyDailyCourse(){
        List<DailyCourseResponseDTO> myScraps = dailyCourseService.getMyScraps(1L);
        return ResponseEntity.status(HttpStatus.OK).body(myScraps);
    }


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
        List<FullCourseResponseDTO> myScraps = fullCourseService.getMyScraps(memberId);
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
        List<DailyCourseResponseDTO> myScraps = dailyCourseService.getMyScraps(memberId);
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

    @GetMapping("/nickname")
    public ResponseEntity<?> checkNickName(@RequestHeader("Authorization") String token) {
        try {
            // 토큰 확인
            System.out.println("Received token: " + token);

            // Bearer 제거 (필요시)
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 서비스에서 닉네임 체크
            MemberDTO memberDTO = memberService.checkNickName(token);
            if (memberDTO.getNickname() == null || memberDTO.getNickname().isEmpty()) {
                // 닉네임이 없는 경우 닉네임 입력 요청
                return ResponseEntity.status(HttpStatus.OK).body("닉네임 입력 필요");
            }
            return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // 예상치 못한 오류 처리
            System.err.println("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

//    // 닉네임 저장
//    @PutMapping("/nickname")
//    public ResponseEntity<?> putNickName(@RequestHeader("Authorization") String token, @RequestBody String nickName) {
//        if (nickName == null || nickName.trim().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임 누락");
//        }
//
//        try {
//            // 닉네임 저장 서비스 호출
//            memberService.saveNickName(token, nickName);
//            return ResponseEntity.status(HttpStatus.OK).body("닉네임 저장 완료");
//        } catch (UsernameNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
    @PutMapping("/nickname")
    public ResponseEntity<?> putNickName(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
        String nickName = request.get("nickName");
        if (nickName == null || nickName.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임 누락");
        }
        try {
            memberService.saveNickName(token, nickName);
            return ResponseEntity.status(HttpStatus.OK).body("닉네임 저장 완료");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 닉네임 중복 확인 API
    @GetMapping("/check-nickname")
    public ResponseEntity<String> checkNickNameDuplicate(@RequestParam String nickName) {
        // 주어진 닉네임으로 Member 조회
        boolean exists = memberService.existsByNickName(nickName); // memberService에서 닉네임 중복 확인 호출
        if (exists) {
            // 닉네임이 이미 존재하면 "닉네임 중복" 메시지를 반환
            return ResponseEntity.status(HttpStatus.OK).body("닉네임 중복");
        } else {
            // 닉네임이 존재하지 않으면 "닉네임 사용 가능" 메시지를 반환
            return ResponseEntity.status(HttpStatus.OK).body("닉네임 사용 가능");
        }
    }

    // 닉네임 수정 API
    @PutMapping("/update-nickname")
    public ResponseEntity<?> updateNickName(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
        String nickName = request.get("nickName"); // 요청 본문에서 닉네임 추출
        if (nickName == null || nickName.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임이 유효하지 않습니다.");
        }

        try {
            // 서비스에서 닉네임 수정 처리
            memberService.updateNickName(token, nickName);
            return ResponseEntity.status(HttpStatus.OK).body("닉네임 수정 완료");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
