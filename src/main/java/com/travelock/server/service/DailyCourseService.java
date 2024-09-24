package com.travelock.server.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.*;
import com.travelock.server.dto.DailyCourseRequestDTO;
import com.travelock.server.exception.base_exceptions.BadRequestException;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.exception.course.AddDailyCourseFavoriteException;
import com.travelock.server.exception.course.AddDailyCourseScrapException;
import com.travelock.server.exception.review.AddReviewException;
import com.travelock.server.repository.DailyCourseFavoriteRepository;
import com.travelock.server.repository.DailyCourseRepository;
import com.travelock.server.repository.DailyCourseScrapRepository;
import com.travelock.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyCourseService {
    private final JPAQueryFactory query;
    private final DailyCourseRepository dailyCourseRepository;
    private final DailyCourseFavoriteRepository dailyCourseFavoriteRepository;
    private final DailyCourseScrapRepository dailyCourseScrapRepository;
    private final MemberRepository memberRepository;
    private final FullCourseService fullCourseService;

    /**
     * 일자별 일정 생성
     */
    public DailyCourse saveCourse(DailyCourseRequestDTO requestDTO) {
        // 유효성 검사
        // 필수값 체크
        if (requestDTO.getFullCourseId() == null || requestDTO.getFullCourseId() == 0
        || requestDTO.getMemberId() == null || requestDTO.getMemberId() == 0) {
            // @TODO 일단 내부 확인용도로 응답메시지 작성
            throw new BadRequestException("fullCourseId or memberId is required");
        }
        // 멤버 조회
        Member member = memberRepository.findById(1L).get(); // 테스트
        // 전체 일정 조회
        FullCourse fullCourse = fullCourseService.findFullCourse(requestDTO.getFullCourseId());

        // DB INSERT
        DailyCourse dailyCourse = new DailyCourse();
        dailyCourse.addDailyCourse(
                requestDTO.getDayNum(),
                member,
                fullCourse
        );
        try {
            return dailyCourseRepository.save(dailyCourse);
        } catch (Exception e) {
            // @TODO Add log

            throw new AddReviewException("저장에 실패했습니다." + e.getMessage() );
        }

    }

    public void setFavorite(Long dailyCourseId, Long memberId) {

        QMember qMember = QMember.member;
        QDailyCourse qDailyCourse = QDailyCourse.dailyCourse;

        Tuple tuple = query.select(qMember, qDailyCourse)
                .from(qMember)
                .join(qDailyCourse).on(qDailyCourse.dailyCourseId.eq(dailyCourseId))
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        if (tuple == null || tuple.get(qMember) == null || tuple.get(qDailyCourse) == null) {
            throw new AddReviewException("Member or DailyCourse not found");
        }

        DailyCourseFavorite dailyCourseFavorite = new DailyCourseFavorite();

        dailyCourseFavorite.addFavorite(
                tuple.get(qMember),
                tuple.get(qDailyCourse)
        );

        try {
            dailyCourseFavoriteRepository.save(dailyCourseFavorite);
        } catch (Exception e) {
            log.error("Failed to add DailyCourseFavorite. ", e);
            throw new AddDailyCourseFavoriteException("Failed to save DailyCourseFavorite");
        }


    }

    public void setScrap(Long dailyCourseId, Long memberId) {

        QMember qMember = QMember.member;
        QDailyCourse qDailyCourse = QDailyCourse.dailyCourse;

        Tuple tuple = query.select(qMember, qDailyCourse)
                .from(qMember)
                .join(qDailyCourse).on(qDailyCourse.dailyCourseId.eq(dailyCourseId))
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        if (tuple == null || tuple.get(qMember) == null || tuple.get(qDailyCourse) == null) {
            throw new AddReviewException("Member or DailyCourse not found");
        }

        DailyCourseScrap dailyCourseScrap = new DailyCourseScrap();

        dailyCourseScrap.addScrap(
                tuple.get(qMember),
                tuple.get(qDailyCourse)
        );

        try {
            dailyCourseScrapRepository.save(dailyCourseScrap);
        } catch (Exception e) {
            log.error("Failed to add DailyCourseScrap. ", e);
            throw new AddDailyCourseScrapException("Failed to save DailyCourseScrap");
        }
    }

    public List<DailyCourseFavorite> getMyFavorites(Long memberId) {
        QDailyCourseFavorite qDailyCourseFavorite = QDailyCourseFavorite.dailyCourseFavorite;

        List<DailyCourseFavorite> dailyCourseFavorites = query
                .selectFrom(qDailyCourseFavorite)
                .where(qDailyCourseFavorite.member.memberId.eq(memberId))
                .fetch();

        if (dailyCourseFavorites == null) {
            throw new ResourceNotFoundException("DailyCourseFavorite not found with Member id: " + memberId);
        }

        return dailyCourseFavorites;
    }

    public List<DailyCourseScrap> getMyScraps(Long memberId) {
        QDailyCourseScrap qDailyCourseFavorite = QDailyCourseScrap.dailyCourseScrap;

        List<DailyCourseScrap> dailyCourseScraps = query
                .selectFrom(qDailyCourseFavorite)
                .where(qDailyCourseFavorite.member.memberId.eq(memberId))
                .fetch();

        if (dailyCourseScraps == null) {
            throw new ResourceNotFoundException("DailyCourseScrap not found with Member id: " + memberId);
        }

        return dailyCourseScraps;

    }
}

