package com.travelock.server.controller;

import com.travelock.server.service.MemberService;
import com.travelock.server.service.cache.ProviderCacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final ProviderCacheService providerCacheService;
    private final MemberService memberService;

    public void register(String email, String nickName){

    }

    @Operation(summary = "Provider 조회",
            tags = {"인증 API - V1"},
            description = "캐시된 추천 소셜로그인 Provider 조회",
            parameters = {
                    @Parameter(name = "email", description = "Provider를 조회할 Email", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "조회 실패", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/provider/{email}")
    public ResponseEntity<?> getProvider(@PathVariable String email){
        String provider =  providerCacheService.getProvider(email);
        if(provider == null){
            provider = memberService.getProvider(email);
        }
        return ResponseEntity.status(HttpStatus.OK).body(provider);
    }
}
