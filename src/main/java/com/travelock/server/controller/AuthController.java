package com.travelock.server.controller;

import com.travelock.server.service.MemberService;
import com.travelock.server.service.cache.ProviderCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<?> getProvider(String email){
        String provider =  providerCacheService.getProvider(email);
        if(provider == null){
            provider = memberService.getProvider(email);
        }
        return ResponseEntity.status(HttpStatus.OK).body(provider);
    }
}
