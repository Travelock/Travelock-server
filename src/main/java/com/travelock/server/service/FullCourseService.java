package com.travelock.server.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.*;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.exception.course.AddFullCourseFavoriteException;
import com.travelock.server.exception.course.AddFullCourseScrapException;
import com.travelock.server.exception.review.AddReviewException;
import com.travelock.server.repository.FullCourseFavoriteRepository;
import com.travelock.server.repository.FullCourseScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FullCourseService {
    private final JPAQueryFactory query;
    private final FullCourseFavoriteRepository fullCourseFavoriteRepository;
    private final FullCourseScrapRepository fullCourseScrapRepository;

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
