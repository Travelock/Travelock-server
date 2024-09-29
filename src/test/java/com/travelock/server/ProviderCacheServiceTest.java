//package com.travelock.server;
//
//import com.travelock.server.exception.base_exceptions.DataAccessFailException;
//import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
//import com.travelock.server.exception.base_exceptions.ServiceUnavailableException;
//import com.travelock.server.service.cache.ProviderCacheService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.RedisConnectionFailureException;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProviderCacheServiceTest {
//
//    @Mock
//    private RedisTemplate<String, String> stringRedisTemplate;
//
//    @Mock
//    private ValueOperations<String, String> valueOperations;
//
//    @InjectMocks
//    private ProviderCacheService providerCacheService;
//
//    @Test
//    @DisplayName("정상적으로 이메일로 provider를 등록할 수 있다.")
//    void setProvider_Success() {
//        // given
//        String email = "test@example.com";
//        String provider = "provider";
//
//        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
//
//        // when
//        providerCacheService.setProvider(email, provider);
//
//        // then
//        verify(valueOperations, times(1)).set("provider:" + email, provider);
//    }
//
//    @Test
//    @DisplayName("Redis 서버 연결 실패 시 ServiceUnavailableException 발생")
//    void setProvider_RedisConnectionFailure() {
//        // given
//        String email = "test@example.com";
//        String provider = "provider";
//
//        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        doThrow(new RedisConnectionFailureException("Redis 연결 실패")).when(valueOperations).set(anyString(), anyString());
//
//        // when & then
//        assertThrows(ServiceUnavailableException.class, () -> providerCacheService.setProvider(email, provider));
//    }
//
//    @Test
//    @DisplayName("데이터 저장 중 DataAccessFailException 발생")
//    void setProvider_DataAccessFail() {
//        // given
//        String email = "test@example.com";
//        String provider = "provider";
//
//        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        doThrow(new DataAccessException("데이터 접근 오류") {}).when(valueOperations).set(anyString(), anyString());
//
//        // when & then
//        assertThrows(DataAccessFailException.class, () -> providerCacheService.setProvider(email, provider));
//    }
//
//    @Test
//    @DisplayName("이메일로 provider를 정상적으로 조회할 수 있다.")
//    void getProvider_Success() {
//        // given
//        String email = "test@example.com";
//        String expectedProvider = "provider";
//
//        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(valueOperations.get("provider:" + email)).thenReturn(expectedProvider);
//
//        // when
//        String actualProvider = providerCacheService.getProvider(email);
//
//        // then
//        verify(valueOperations, times(1)).get("provider:" + email);
//        assertEquals(expectedProvider, actualProvider);
//    }
//
//    @Test
//    @DisplayName("등록된 provider가 없는 경우 ResourceNotFoundException 발생")
//    void getProvider_ResourceNotFound() {
//        // given
//        String email = "test@example.com";
//
//        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(valueOperations.get("provider:" + email)).thenReturn(null);
//
//        // when & then
//        assertThrows(ResourceNotFoundException.class, () -> providerCacheService.getProvider(email));
//    }
//
//    @Test
//    @DisplayName("Redis 서버 연결 실패 시 ServiceUnavailableException 발생")
//    void getProvider_RedisConnectionFailure() {
//        // given
//        String email = "test@example.com";
//
//        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        doThrow(new RedisConnectionFailureException("Redis 연결 실패")).when(valueOperations).get(anyString());
//
//        // when & then
//        assertThrows(ServiceUnavailableException.class, () -> providerCacheService.getProvider(email));
//    }
//
//    @Test
//    @DisplayName("데이터 조회 중 DataAccessFailException 발생")
//    void getProvider_DataAccessFail() {
//        // given
//        String email = "test@example.com";
//
//        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        doThrow(new DataAccessException("데이터 접근 오류") {}).when(valueOperations).get(anyString());
//
//        // when & then
//        assertThrows(DataAccessFailException.class, () -> providerCacheService.getProvider(email));
//    }
//}
