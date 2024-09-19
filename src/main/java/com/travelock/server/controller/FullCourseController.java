package com.travelock.server.controller;

import com.travelock.server.service.FullCourseService;
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

    @PostMapping("/favorite/{fullCourseId}")
    public ResponseEntity<?> setFullCourseFavorite(@PathVariable Long fullCourseId, @RequestParam Long memberId){
        fullCourseService.setFavorite(fullCourseId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("좋아요 성공");
    }

    @PostMapping("/scrap/{fullCourseId}")
    public ResponseEntity<?> setFullCourseScrap(@PathVariable Long fullCourseId, @RequestParam Long memberId){
        fullCourseService.setScrap(fullCourseId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body("스크랩 성공");

    }

}
