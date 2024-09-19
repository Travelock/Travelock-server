package com.travelock.server.controller;


import com.travelock.server.domain.DailyCourseFavorite;
import com.travelock.server.domain.DailyCourseScrap;
import com.travelock.server.domain.FullCourseFavorite;
import com.travelock.server.domain.FullCourseScrap;
import com.travelock.server.dto.SmallBlockReviewDto;
import com.travelock.server.service.DailyCourseService;
import com.travelock.server.service.FullCourseService;
import com.travelock.server.service.SmallBlockReviewService;
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

    public void setNickName(String nickName){}
    public void leave(Long memberId){}

    @GetMapping("/course/full/favorites/{memberId}")
    public ResponseEntity<?> getMyFullCourseFavorites(@PathVariable Long memberId){
        List<FullCourseFavorite> myFavorites = fullCourseService.getMyFavorites(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(myFavorites);

    }
    @GetMapping("/course/daily/favorites/{memberId}")
    public ResponseEntity<?> getMyDailyCourseFavorites(@PathVariable Long memberId){
        List<DailyCourseFavorite> myFavorites = dailyCourseService.getMyFavorites(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(myFavorites);

    }
    @GetMapping("/course/full/scraps/{memberId}")
    public ResponseEntity<?> getMyFullCourseScraps(@PathVariable Long memberId){
        List<FullCourseScrap> myScraps = fullCourseService.getMyScraps(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(myScraps);

    }
    @GetMapping("/course/daily/scraps/{memberId}")
    public ResponseEntity<?> getMyDailyCourseScraps(@PathVariable Long memberId){
        List<DailyCourseScrap> myScraps = dailyCourseService.getMyScraps(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(myScraps);

    }


    @GetMapping("/block/small/review/{memberId}")
    public ResponseEntity<?> getMySmallBlockReviews(@PathVariable Long memberId){
        List<SmallBlockReviewDto> reviews = smallBlockReviewService.getMyReviews(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }



}
