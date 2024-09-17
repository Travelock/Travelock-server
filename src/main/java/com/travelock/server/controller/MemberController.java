package com.travelock.server.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    public void login(String email){}
    public void register(String email){}
    public void setNickName(String nickName){}
    public void leave(Long memberId){}

    public void getMyFullCourseFavorites(){}
    public void getMyDailyCourseFavorites(){}
    public void getMyFullCourseScraps(){}
    public void getMyDailyCourseScraps(){}

    public void getMySmallBlockReviews(){}
}
