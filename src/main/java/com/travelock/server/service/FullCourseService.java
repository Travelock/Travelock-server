package com.travelock.server.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.*;
import com.travelock.server.dto.FullCourseRequestDTO;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.exception.course.AddFullCourseFavoriteException;
import com.travelock.server.exception.course.AddFullCourseScrapException;
import com.travelock.server.exception.review.AddReviewException;
import com.travelock.server.repository.FullCourseFavoriteRepository;
import com.travelock.server.repository.FullCourseRepository;
import com.travelock.server.repository.FullCourseScrapRepository;
import com.travelock.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FullCourseService {
    private final JPAQueryFactory query;
    private final FullCourseRepository fullCourseRepository;
    private final FullCourseFavoriteRepository fullCourseFavoriteRepository;
    private final FullCourseScrapRepository fullCourseScrapRepository;
    private final MemberRepository memberRepository;


    /**
     * 멤버가 생성한 Full Course 조회
     */
    public List<FullCourse> findMemberFullCourses(Long memberId) {
        QFullCourse qFullCourse = QFullCourse.fullCourse;
        // 특정 멤버가 생성한 전체 일정을 최근 생성일자순으로 조회
        List<FullCourse> fullCourses = query
                .selectFrom(qFullCourse)
                .where(qFullCourse.member.memberId.eq(memberId))
                .orderBy(qFullCourse.fullCourseId.desc())
                .fetch(); // 데이터가 없으면 빈리스트 반환

        return fullCourses;
    }

    /**
     * FullCourse ID로 Full Course 조회
     */
    public FullCourse findFullCourse(Long fullCourseId) {
        QFullCourse qFullCourse = QFullCourse.fullCourse;
        // 특정 멤버가 생성한 전체 일정을 최근 생성일자순으로 조회
        FullCourse fullCourse = query
                .selectFrom(qFullCourse)
                .where(qFullCourse.fullCourseId.eq(fullCourseId))
                .fetchOne(); // 데이터가 없으면 빈리스트 반환

        if (fullCourse == null) {
            throw new ResourceNotFoundException("Full Course not found by ID("+fullCourseId+")");
        }

        return fullCourse;
    }

    /**
     * 전체일정 생성
     */
    public FullCourse saveCourse(FullCourseRequestDTO requestDTO) {
        // 유효성 검사
        // @TODO title Null | 빈 문자열인 경우 정책
        if (requestDTO.getTitle() == null || requestDTO.getTitle().isBlank()) {
            // 일단 임의 값 설정
            requestDTO.setTitle("임의 타이틀");
        }
        // @TODO 멤버 조회
        Member member = memberRepository.findById(1L).get(); // 테스트

        // DB INSERT
        FullCourse fullCourse = new FullCourse();
        fullCourse.addFullCourse(
                requestDTO.getTitle(),
                member
        );
        try {
            return fullCourseRepository.save(fullCourse);
        } catch (Exception e) {
            // @TODO Add log

            throw new AddReviewException("저장에 실패했습니다." + e.getMessage() );
        }
    }

    public void setFavorite(Long fullCourseId, Long memberId) {

        QMember qMember = QMember.member;
        QFullCourse qFullCourse = QFullCourse.fullCourse;

        Tuple tuple = query.select(qMember, qFullCourse)
                .from(qMember)
                .join(qFullCourse).on(qFullCourse.fullCourseId.eq(fullCourseId))
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        if (tuple == null || tuple.get(qMember) == null || tuple.get(qFullCourse) == null) {
            throw new AddReviewException("Member or FullCourse not found");
        }

        FullCourseFavorite fullCourseFavorite = new FullCourseFavorite();

        fullCourseFavorite.addFavorite(
                tuple.get(qMember),
                tuple.get(qFullCourse)
        );

        try {
            fullCourseFavoriteRepository.save(fullCourseFavorite);
        } catch (Exception e) {
            log.error("Failed to add FullCourseFavorite. ", e);
            throw new AddFullCourseFavoriteException("Failed to save FullCourseFavorite");
        }


    }
    public void setScrap(Long fullCourseId, Long memberId) {

        QMember qMember = QMember.member;
        QFullCourse qFullCourse = QFullCourse.fullCourse;

        Tuple tuple = query.select(qMember, qFullCourse)
                .from(qMember)
                .join(qFullCourse).on(qFullCourse.fullCourseId.eq(fullCourseId))
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        if (tuple == null || tuple.get(qMember) == null || tuple.get(qFullCourse) == null) {
            throw new AddReviewException("Member or FullCourse not found");
        }

        FullCourseScrap fullCourseScrap = new FullCourseScrap();

        fullCourseScrap.addScrap(
                tuple.get(qMember),
                tuple.get(qFullCourse)
        );

        try {
            fullCourseScrapRepository.save(fullCourseScrap);
        } catch (Exception e) {
            log.error("Failed to add FullCourseScrap. ", e);
            throw new AddFullCourseScrapException("Failed to save FullCourseScrap");
        }
    }
    public List<FullCourseFavorite> getMyFavorites(Long memberId) {
        QFullCourseFavorite qFullCourseFavorite = QFullCourseFavorite.fullCourseFavorite;

        List<FullCourseFavorite> fullCourseFavorites = query
                .selectFrom(qFullCourseFavorite)
                .where(qFullCourseFavorite.member.memberId.eq(memberId))
                .fetch();

        if (fullCourseFavorites == null) {
            throw new ResourceNotFoundException("FullCourseFavorite not found with Member id: " + memberId);
        }

        return fullCourseFavorites;
    }
    public List<FullCourseScrap> getMyScraps(Long memberId) {
        QFullCourseScrap qFullCourseScrap = QFullCourseScrap.fullCourseScrap;

        List<FullCourseScrap> fullCourseScraps = query
                .selectFrom(qFullCourseScrap)
                .where(qFullCourseScrap.member.memberId.eq(memberId))
                .fetch();

        if (fullCourseScraps == null) {
            throw new ResourceNotFoundException("FullCourseScrap not found with Member id: " + memberId);
        }

        return fullCourseScraps;
    }
}
