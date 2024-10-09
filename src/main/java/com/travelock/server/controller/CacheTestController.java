package com.travelock.server.controller;

import com.travelock.server.domain.DailyCourse;
import com.travelock.server.domain.FullCourse;
import com.travelock.server.dto.course.cache.DailyCourseCacheDto;
import com.travelock.server.dto.course.cache.FullCourseCacheDto;
import com.travelock.server.service.cache.CourseRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
public class CacheTestController {
    private final CourseRecommendService courseRecommendService;



    @PostMapping("/full")
    public void cacheFullRecommends(){
        courseRecommendService.updateTopFullCourses();
    }

    @PostMapping("/daily")
    public void cacheDailyRecommends(){
        courseRecommendService.updateTopDailyCourses();
    }


    @GetMapping("/full")
    public ResponseEntity<?> getFullRecommends(){
        List<FullCourseCacheDto> topFullCoursesFromCache = courseRecommendService.getTopFullCoursesFromCache();
        return ResponseEntity.status(HttpStatus.OK).body(topFullCoursesFromCache);
    }

    @GetMapping("/daily")
    public ResponseEntity<?> getDailyRecommends(){
        List<DailyCourseCacheDto> topDailyCoursesFromCache = courseRecommendService.getTopDailyCoursesFromCache();
        return ResponseEntity.status(HttpStatus.OK).body(topDailyCoursesFromCache);
    }
}
