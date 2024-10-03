//package com.travelock.server;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.travelock.server.domain.DailyCourse;
//import com.travelock.server.domain.FullCourse;
//import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
//import com.travelock.server.service.cache.CourseRecommendService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CourseRecommendServiceTest {
//
//    @Mock
//    private JPAQueryFactory query;
//
//    @Mock
//    private RedisTemplate<String, Object> jsonRedisTemplate;
//
//    @Mock
//    private ValueOperations<String, Object> valueOperations;
//
//    @InjectMocks
//    private CourseRecommendService courseRecommendService;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("FullCourse 상위 10개를 Redis에 캐싱할 수 있다.")
//    void updateTopFullCourses_Success() {
//        // given
//        when(jsonRedisTemplate.opsForValue()).thenReturn(valueOperations);
//
//        // when
//        courseRecommendService.updateTopFullCourses();
//
//        // then
//        verify(valueOperations, times(1)).set(eq("popular:full"), any());
//    }
//
//    @Test
//    @DisplayName("Redis에서 캐싱된 FullCourse 리스트를 가져올 수 있다.")
//    void getTopFullCoursesFromCache_Success() {
//        // given
//        List<FullCourse> mockCourses = new ArrayList<>();
//        mockCourses.add(new FullCourse());
//        Object cachedData = mockCourses;
//
//        when(jsonRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(valueOperations.get("popular:full")).thenReturn(cachedData);
//        when(objectMapper.convertValue(cachedData, new TypeReference<List<FullCourse>>() {}))
//                .thenReturn(mockCourses);
//
//        // when
//        List<FullCourse> fullCourses = courseRecommendService.getTopFullCoursesFromCache();
//
//        // then
//        assertNotNull(fullCourses);
//        assertEquals(1, fullCourses.size());
//    }
//
//    @Test
//    @DisplayName("캐싱된 FullCourse가 없을 경우 ResourceNotFoundException 발생")
//    void getTopFullCoursesFromCache_ResourceNotFoundException() {
//        // given
//        when(jsonRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(valueOperations.get("popular:full")).thenReturn(null);
//
//        // when & then
//        assertThrows(ResourceNotFoundException.class, () -> courseRecommendService.getTopFullCoursesFromCache());
//    }
//
//    @Test
//    @DisplayName("캐시 데이터 변환 중 오류가 발생할 경우 RuntimeException 발생")
//    void getTopFullCoursesFromCache_ConversionFailure() {
//        // given
//        Object cachedData = new Object();
//
//        when(jsonRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(valueOperations.get("popular:full")).thenReturn(cachedData);
//        when(objectMapper.convertValue(cachedData, new TypeReference<List<FullCourse>>() {}))
//                .thenThrow(new IllegalArgumentException("변환 실패"));
//
//        // when & then
//        assertThrows(RuntimeException.class, () -> courseRecommendService.getTopFullCoursesFromCache());
//    }
//
//    @Test
//    @DisplayName("DailyCourse 상위 10개를 Redis에 캐싱할 수 있다.")
//    void updateTopDailyCourses_Success() {
//        // given
//        when(jsonRedisTemplate.opsForValue()).thenReturn(valueOperations);
//
//        // when
//        courseRecommendService.updateTopDailyCourses();
//
//        // then
//        verify(valueOperations, times(1)).set(eq("popular:daily"), any());
//    }
//
//    @Test
//    @DisplayName("Redis에서 캐싱된 DailyCourse 리스트를 가져올 수 있다.")
//    void getTopDailyCoursesFromCache_Success() {
//        // given
//        List<DailyCourse> mockCourses = new ArrayList<>();
//        mockCourses.add(new DailyCourse());
//        Object cachedData = mockCourses;
//
//        when(jsonRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(valueOperations.get("popular:daily")).thenReturn(cachedData);
//        when(objectMapper.convertValue(cachedData, new TypeReference<List<DailyCourse>>() {}))
//                .thenReturn(mockCourses);
//
//        // when
//        List<DailyCourse> dailyCourses = courseRecommendService.getTopDailyCoursesFromCache();
//
//        // then
//        assertNotNull(dailyCourses);
//        assertEquals(1, dailyCourses.size());
//    }
//
//    @Test
//    @DisplayName("캐싱된 DailyCourse가 없을 경우 ResourceNotFoundException 발생")
//    void getTopDailyCoursesFromCache_ResourceNotFoundException() {
//        // given
//        when(jsonRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(valueOperations.get("popular:daily")).thenReturn(null);
//
//        // when & then
//        assertThrows(ResourceNotFoundException.class, () -> courseRecommendService.getTopDailyCoursesFromCache());
//    }
//
//    @Test
//    @DisplayName("DailyCourse 캐시 데이터 변환 중 오류가 발생할 경우 RuntimeException 발생")
//    void getTopDailyCoursesFromCache_ConversionFailure() {
//        // given
//        Object cachedData = new Object();
//
//        when(jsonRedisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(valueOperations.get("popular:daily")).thenReturn(cachedData);
//        when(objectMapper.convertValue(cachedData, new TypeReference<List<DailyCourse>>() {}))
//                .thenThrow(new IllegalArgumentException("변환 실패"));
//
//        // when & then
//        assertThrows(RuntimeException.class, () -> courseRecommendService.getTopDailyCoursesFromCache());
//    }
//}
