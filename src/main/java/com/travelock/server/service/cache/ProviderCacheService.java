package com.travelock.server.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderCacheService {
    private final RedisTemplate<String, String> stringRedisTemplate;

    private final String PROVIDER_BASE = "provider:";

    //provider를 email을 key로 redis에 등록
    public void setProvider(String email, String provider){
        stringRedisTemplate.opsForValue().set(email, provider);

    }

    //email로 등록된 provider 조회
    public String getProvider(String email){
        return stringRedisTemplate.opsForValue().get(email);
    }

}
