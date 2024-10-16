package com.travelock.server.controller;

import com.travelock.server.domain.Member;
import com.travelock.server.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.ProviderNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor
public class AuthController {
//    private final MemberService memberService;
//    private final AuthService authService;
//
//    @Operation(summary = "회원가입",
//            tags = {"인증 API - V1"},
//            description = "회원가입",
//            parameters = {
//                    @Parameter(name = "email", description = "식별자로 사용될 이메일", required = true, in = ParameterIn.PATH),
//                    @Parameter(name = "nickName", description = "서비스에서 사용할 닉네임", required = true, in = ParameterIn.PATH),
//            },
//            responses = {
//                    @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(mediaType = "application/json")),
//                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
//            })
//    @PostMapping("/member/{email}/{nickName}")
//    public ResponseEntity<?> register(@PathVariable String email, @PathVariable String nickName){
//        authService.register(email, nickName);
//        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
//    }
//
//    public void verifyEmail(String email){}
//    public void checkEmail(){}
//
//    @Operation(summary = "이메일 중복 조회",
//            tags = {"인증 API - V1"},
//            description = "회원가입시 닉네임 중복 조회",
//            parameters = {
//                    @Parameter(name = "email", description = "중복 확인할 이메일", required = true, in = ParameterIn.PATH),
//            },
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "이메일 사용 가능", content = @Content(mediaType = "application/json")),
//                    @ApiResponse(responseCode = "409", description = "이메일 중복", content = @Content(mediaType = "application/json")),
//                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
//            })
//    @GetMapping("/validate/{email}/email")
//    public void validateEmail(@PathVariable String email){
//        authService.validateEmail(email);
//    }
//
//    @Operation(summary = "닉네임 중복 조회",
//            tags = {"인증 API - V1"},
//            description = "회원가입시 닉네임 중복 조회",
//            parameters = {
//                    @Parameter(name = "nickName", description = "중복 확인할 닉네임", required = true, in = ParameterIn.PATH),
//            },
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "닉네임 사용 가능", content = @Content(mediaType = "application/json")),
//                    @ApiResponse(responseCode = "409", description = "닉네임 중복", content = @Content(mediaType = "application/json")),
//                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json")),
//            })
//    @GetMapping("/validate/{nickName}/nickname")
//    public void validateNickName(@PathVariable String nickName){
//        authService.validateNickName(nickName);
//    }
//
//
//-------------------------------------------------------------------------------------------------------------------
    private final MemberRepository memberRepository;

    public AuthController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping("/check-account")
    public ResponseEntity<?> checkAccount(@RequestBody Map<String, String> request) {
//        log.info("Check account request: {}", request);
        String account = request.get("account");
        System.out.println(account);

        // 이메일로 사용자 정보 조회 (이메일 중복 가능성을 고려하여 List로 받음)
        List<Member> users = memberRepository.findAllByEmail(account);
        System.out.println(users);

        if (!users.isEmpty()) {
            // 여러 사용자 계정을 돌면서 각 소셜 제공자 정보를 추출
            List<String> providers = users.stream()
                    .map(user -> user.getProvider().split(" ")[0]) // provider에서 소셜 제공자 추출
                    .distinct() // 중복 소셜 제공자 제거
                    .collect(Collectors.toList());

//            log.info("Found providers for email {}: {}", account, providers);

            // 소셜 제공자 정보 배열로 반환
            return ResponseEntity.ok(Collections.singletonMap("providers", providers));
        } else {
            // 등록되지 않은 계정일 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("등록된 소셜 로그인 제공자가 없습니다.");
        }
    }

    @GetMapping("/nickName")
    public ResponseEntity<?> checkNickName(){
        Member member = memberRepository.findById(1L).orElseThrow(() -> new ProviderNotFoundException("Member not Found"));
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    @PutMapping("/member/{nickName}")
    public ResponseEntity<?> putNickName(@PathVariable String nickName){
        if(nickName == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임 누락");
        }

        return ResponseEntity.status(HttpStatus.OK).body("닉네임 저장 완료");
    }
}
