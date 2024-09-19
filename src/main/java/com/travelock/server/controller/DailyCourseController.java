package com.travelock.server.controller;

import com.travelock.server.service.DailyCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course/daily")
@RequiredArgsConstructor
@Slf4j
public class DailyCourseController {
    private final DailyCourseService dailyCourseService;

    @PostMapping("/favorite/{dailyCourseId}")
    public ResponseEntity<?> setDailyCourseFavorite(@PathVariable Long dailyCourseId, @RequestParam Long memberId){
        dailyCourseService.setFavorite(dailyCourseId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("좋아요 성공");
    }
    @PostMapping("/scrap/{dailyCourseId}")
    public ResponseEntity<?> setDailyCourseScrap(@PathVariable Long dailyCourseId, @RequestParam Long memberId){
        dailyCourseService.setScrap(dailyCourseId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("스크랩 성공");
    }

}
