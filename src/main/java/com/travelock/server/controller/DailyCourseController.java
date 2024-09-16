package com.travelock.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course/daily")
@RequiredArgsConstructor
@Slf4j
public class DailyCourseController {

    public void setDailyCourseFavorite(){}
    public void setDailyCourseScrap(){}
    public void getHighFavoriteDailyCourses(){}
    public void getHighScrapDailyCourses(){}

}
