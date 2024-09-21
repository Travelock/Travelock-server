package com.travelock.server.service.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.*;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseRecommendService {

    private final JPAQueryFactory query;
    private final RedisTemplate<String, Object> jsonRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 사용

    private static final String REDIS_FULL_KEY = "popular:full";
    private static final String REDIS_DAILY_KEY = "popular:daily";

    // 매일 자정에 좋아요와 스크랩 수가 높은 FullCourse 캐싱
    @Scheduled(cron = "0 0 0 * * ?")
    // FullCourse 상위 10개 조회 및 Redis에 캐싱
    public void updateTopFullCourses() {
        try {
            QFullCourse qFullCourse = QFullCourse.fullCourse;
            QFullCourseFavorite qFullCourseFavorite = QFullCourseFavorite.fullCourseFavorite;
            QFullCourseScrap qFullCourseScrap = QFullCourseScrap.fullCourseScrap;

            // 좋아요와 스크랩 수가 높은 상위 10개 조회
            List<FullCourse> topFullCourses = query
                    .select(qFullCourse)
                    .from(qFullCourse)
                    .leftJoin(qFullCourseFavorite).on(qFullCourseFavorite.fullCourse.eq(qFullCourse))
                    .leftJoin(qFullCourseScrap).on(qFullCourseScrap.fullCourse.eq(qFullCourse))
                    .groupBy(qFullCourse.fullCourseId)
                    .orderBy(qFullCourseFavorite.count().add(qFullCourseScrap.count()).desc()) // 좋아요와 스크랩 수를 합산하여 정렬
                    .limit(10) // 상위 10개만 조회
                    .fetch();

            // Redis에 캐싱
            jsonRedisTemplate.opsForValue().set(REDIS_FULL_KEY, topFullCourses);
        } catch (Exception e) {
            log.error("전체일정 캐시 실패{}", e);
        }
    }

    // Redis에서 캐싱된 FullCourse 리스트를 가져오기
    public List<FullCourse> getTopFullCoursesFromCache() {
        Object cachedData = jsonRedisTemplate.opsForValue().get(REDIS_FULL_KEY);
        if (cachedData == null) {
            throw new ResourceNotFoundException("추천 전체일정을 찾을 수 없습니다.");
        }
        // 안전한 타입 변환을 위해 ObjectMapper 사용하여 JSON 역직렬화
        try {
            return objectMapper.convertValue(cachedData, new TypeReference<List<FullCourse>>() {
            });
        } catch (IllegalArgumentException e) {
            // 변환 중 예외 발생 시 처리
            throw new RuntimeException("캐시 데이터를 변환하는 데 실패했습니다.", e);
        }
    }

    // 매일 01시에 좋아요와 스크랩 수가 높은 DailyCourse 캐싱
    @Scheduled(cron = "0 0 1 * * ?")
    // DailyCourse 상위 10개 조회 및 Redis에 캐싱
    public void updateTopDailyCourses() {
        try {
            QDailyCourse qDailyCourse = QDailyCourse.dailyCourse;
            QDailyCourseFavorite qDailyCourseFavorite = QDailyCourseFavorite.dailyCourseFavorite;
            QDailyCourseScrap qDailyCourseScrap = QDailyCourseScrap.dailyCourseScrap;

            // 좋아요와 스크랩 수가 높은 상위 10개 조회
            List<DailyCourse> topDailyCourses = query
                    .select(qDailyCourse)
                    .from(qDailyCourse)
                    .leftJoin(qDailyCourseFavorite).on(qDailyCourseFavorite.dailyCourse.eq(qDailyCourse))
                    .leftJoin(qDailyCourseScrap).on(qDailyCourseScrap.dailyCourse.eq(qDailyCourse))
                    .groupBy(qDailyCourse.dailyCourseId)
                    .orderBy(qDailyCourseFavorite.count().add(qDailyCourseScrap.count()).desc()) // 좋아요와 스크랩 수를 합산하여 정렬
                    .limit(10) // 상위 10개만 조회
                    .fetch();

            // Redis에 캐싱
            jsonRedisTemplate.opsForValue().set(REDIS_DAILY_KEY, topDailyCourses);
        } catch (Exception e) {
            log.error("일일일정 캐시 실패 {}", e);
        }
    }

    // Redis에서 캐싱된 DailyCourse 리스트를 가져오기
    public List<DailyCourse> getTopDailyCoursesFromCache() {
        Object cachedData = jsonRedisTemplate.opsForValue().get(REDIS_DAILY_KEY);
        if (cachedData == null) {
            throw new ResourceNotFoundException("추천 일일일정을 찾을 수 없습니다.");
        }
        try {
            return objectMapper.convertValue(cachedData, new TypeReference<List<DailyCourse>>() {
            });
        } catch (IllegalArgumentException e) {
            // 변환 중 예외 발생 시 처리
            throw new RuntimeException("캐시 데이터를 변환하는 데 실패했습니다.", e);
        }
    }
}