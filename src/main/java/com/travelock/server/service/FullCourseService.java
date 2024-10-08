package com.travelock.server.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.*;
import com.travelock.server.dto.DailyCourseRequestDTO;
import com.travelock.server.dto.FullCourseRequestDTO;
import com.travelock.server.dto.course.full_create.DailyCourseDto;
import com.travelock.server.dto.course.full_create.FullCourseCreateDto;
import com.travelock.server.exception.GlobalExceptionHandler;
import com.travelock.server.exception.base_exceptions.DataAccessFailException;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.exception.course.AddFullCourseFavoriteException;
import com.travelock.server.exception.course.AddFullCourseScrapException;
import com.travelock.server.exception.course.EmptyTitleException;
import com.travelock.server.exception.review.AddReviewException;
import com.travelock.server.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FullCourseService {
    private final JPAQueryFactory query;
    private final FullCourseRepository fullCourseRepository;
    private final FullCourseFavoriteRepository fullCourseFavoriteRepository;
    private final FullCourseScrapRepository fullCourseScrapRepository;
    private final FullAndDailyCourseConnectRepository fullAndDailyCourseConnectRepository;
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

    /**전체일정 생성
     * -> 일일일정 생성시 연결객체 생성됨. 전체일정은 간단하게 저장해도 될듯
     * */
    public FullCourse saveFullCourse(FullCourseRequestDTO createDto){

        if (createDto.getTitle() == null || createDto.getTitle().isBlank()) {
            throw new EmptyTitleException("Title is empty");
        }

        //수정필요
        Long memberId = 1L;

        //초기화
        QMember qMember = QMember.member;

        //현재 사용자 조회 ------------------------------------------------------------------------------DB SELECT (1)
        Member member = query.selectFrom(qMember).where(qMember.memberId.eq(memberId)).fetchOne();

        if(member == null) {
            throw new ResourceNotFoundException("Member not found");
        }

        FullCourse fullCourse = new FullCourse();
        fullCourse.addFullCourse(
                createDto.getTitle(),
                member
        );
        //--------------------------------------------------------------------------------------------DB INSERT (1)
        return fullCourseRepository.save(fullCourse);
    }

    /**제목 수정*/
    public void modifyTitle(FullCourseRequestDTO requestDTO){
        QFullCourse qFullCourse = QFullCourse.fullCourse;

        if (requestDTO.getTitle() == null || requestDTO.getTitle().isBlank()) {
            //404
            throw new EmptyTitleException("Title is empty");
        }

        long result = query.update(qFullCourse)
                .set(qFullCourse.title, requestDTO.getTitle())
                .where(
                        qFullCourse.fullCourseId.eq(requestDTO.getFullCourseId())
                        .and(qFullCourse.member.memberId.eq(requestDTO.getMemberId()))
                )
                .execute();

        if(result == 0){
            //500
            throw new DataAccessFailException("FullCourse update failed");
        }
    }

    /**전체일정 수정*/
    public FullCourse modifyFullCourse(){



        return null;
    }



    /**좋아요 설정*/
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

    /**스크랩 설정*/
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

    /**좋아요한 전체일정 목록*/
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

    /**스크랩한 전체일정 목로*/
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
