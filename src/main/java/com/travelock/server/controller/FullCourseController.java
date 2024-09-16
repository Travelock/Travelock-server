package com.travelock.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course/full")
@RequiredArgsConstructor
@Slf4j
public class FullCourseController {
    public void setFullCourseFavorite(){}
    public void setFullCourseScrap(){}
    public void getHighFavoriteFullCourses(){}
    public void getHighScrapFullCourses(){}

}
