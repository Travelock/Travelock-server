package com.travelock.server.controller;

import com.travelock.server.service.AuthService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final ProviderCacheService providerCacheService;
    private final MemberService memberService;
    private final AuthService authService;

    @Operation(summary = "회원가입",
            tags = {"인증 API - V1"},
            description = "회원가입",
            parameters = {
                    @Parameter(name = "email", description = "식별자로 사용될 이메일", required = true, in = ParameterIn.PATH),
                    @Parameter(name = "nickName", description = "서비스에서 사용할 닉네임", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @PostMapping("/member/{email}/{nickName}")
    public ResponseEntity<?> register(@PathVariable String email, @PathVariable String nickName){
        authService.register(email, nickName);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    public void verifyEmail(String email){}
    public void checkEmail(){}

    @Operation(summary = "이메일 중복 조회",
            tags = {"인증 API - V1"},
            description = "회원가입시 닉네임 중복 조회",
            parameters = {
                    @Parameter(name = "email", description = "중복 확인할 이메일", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 사용 가능", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "409", description = "이메일 중복", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/validate/{email}/email")
    public void validateEmail(@PathVariable String email){
        authService.validateEmail(email);
    }

    @Operation(summary = "닉네임 중복 조회",
            tags = {"인증 API - V1"},
            description = "회원가입시 닉네임 중복 조회",
            parameters = {
                    @Parameter(name = "nickName", description = "중복 확인할 닉네임", required = true, in = ParameterIn.PATH),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "닉네임 사용 가능", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "409", description = "닉네임 중복", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
            })
    @GetMapping("/validate/{nickName}/nickname")
    public void validateNickName(@PathVariable String nickName){
        authService.validateNickName(nickName);
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
        /*캐시에서 조회 */
        String provider =  providerCacheService.getProvider(email);
        /*캐시에 없으면 DB에서 조회*/
        if(provider == null){
            provider = memberService.getProvider(email);
        }
        return ResponseEntity.status(HttpStatus.OK).body(provider);
    }
}
