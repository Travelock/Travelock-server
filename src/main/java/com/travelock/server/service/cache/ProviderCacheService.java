package com.travelock.server.service.cache;

import com.travelock.server.exception.base_exceptions.DataAccessFailException;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.exception.base_exceptions.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderCacheService {
    private final RedisTemplate<String, String> stringRedisTemplate;

    private final String PROVIDER_BASE = "provider:";

    //provider를 email을 key로 redis에 등록
    public void setProvider(String email, String provider){
        try {
            stringRedisTemplate.opsForValue().set(PROVIDER_BASE + email, provider);
        } catch (RedisConnectionFailureException e) {
            log.error("Redis 서버에 연결할 수 없습니다.");
            throw new ServiceUnavailableException("Redis 서버에 연결할 수 없습니다.");
        } catch (DataAccessException e) {
            log.error("Redis에 데이터를 저장하는 중 오류가 발생했습니다.");
            throw new DataAccessFailException("Redis에 데이터를 저장하는 중 오류가 발생했습니다.");
        }
    }

    //email로 등록된 provider 조회
    public String getProvider(String email){
        try {
            String provider = stringRedisTemplate.opsForValue().get(PROVIDER_BASE + email);
            if (provider == null) {
                log.error("등록된 provider가 없습니다 : {}", email);
                throw new ResourceNotFoundException("등록된 provider가 없습니다.");
            }
            return provider;
        } catch (RedisConnectionFailureException e) {
            log.error("Redis 서버에 연결할 수 없습니다.");
            throw new ServiceUnavailableException("Redis 서버에 연결할 수 없습니다.");
        } catch (DataAccessException e) {
            log.error("Redis에서 데이터를 조회하는 중 오류가 발생했습니다.");
            throw new DataAccessFailException("Redis에서 데이터를 조회하는 중 오류가 발생했습니다.");
        }
    }

}
