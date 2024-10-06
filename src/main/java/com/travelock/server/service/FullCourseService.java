package com.travelock.server.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.*;
import com.travelock.server.dto.DailyCourseRequestDTO;
import com.travelock.server.dto.FullCourseRequestDTO;
import com.travelock.server.dto.course.full_modify.FullCourseModifyDto;
import com.travelock.server.exception.base_exceptions.BadRequestException;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.exception.course.AddFullCourseFavoriteException;
import com.travelock.server.exception.course.AddFullCourseScrapException;
import com.travelock.server.exception.course.EmptyTitleException;
import com.travelock.server.exception.review.AddReviewException;
import com.travelock.server.repository.FullAndDailyCourseConnectRepository;
import com.travelock.server.repository.FullCourseFavoriteRepository;
import com.travelock.server.repository.FullCourseRepository;
import com.travelock.server.repository.FullCourseScrapRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FullCourseService {
    private final JPAQueryFactory query;
    private final FullCourseRepository fullCourseRepository;
    private final FullCourseFavoriteRepository fullCourseFavoriteRepository;
    private final FullAndDailyCourseConnectRepository fullAndDailyCourseConnectRepository;
    private final FullCourseScrapRepository fullCourseScrapRepository;
    private final EntityManager em;

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
    public FullCourse modifyTitle(FullCourseRequestDTO requestDTO){
        QFullCourse qFullCourse = QFullCourse.fullCourse;

        if (requestDTO.getTitle() == null || requestDTO.getTitle().isBlank()) {
            //404
            throw new EmptyTitleException("Title is empty");
        }

        FullCourse fullCourse = fullCourseRepository.findById(requestDTO.getFullCourseId()).orElseThrow(() -> new ResourceNotFoundException("FullCourse not found"));

        System.out.println(fullCourse.getFullCourseId());
        fullCourse.changeTitle(requestDTO.getTitle());
        return fullCourseRepository.save(fullCourse);
    }

    /**전체일정 수정*/
    public FullCourse modifyFullCourse(List<FullCourseModifyDto> requestDTO) {

        Long memberId = 1L; // 내 사용자 ID

        QMember qMember = QMember.member;
        QFullCourse qFullCourse = QFullCourse.fullCourse;
        QDailyCourse qDailyCourse = QDailyCourse.dailyCourse;
        QFullAndDailyCourseConnect qFullAndDailyCourseConnect = QFullAndDailyCourseConnect.fullAndDailyCourseConnect;

        // fullCourse 조회
        FullCourse fullCourse = query.selectFrom(qFullCourse)
                .where(qFullCourse.fullCourseId.eq(requestDTO.get(0).getFullCourseId()))
                .fetchOne();

        // 모든 일일 코스 ID 수집
        List<Long> dailyCourseIds = requestDTO.stream()
                .map(FullCourseModifyDto::getDailyCourseId)
                .collect(Collectors.toList());

        // 모든 dayNum 수집
        List<Integer> dayNums = requestDTO.stream()
                .map(FullCourseModifyDto::getDayNum)
                .collect(Collectors.toList());

        // 기존 연결 객체 중 해당 fullCourseId와 dayNum이 일치하는 것만 조회
        List<FullAndDailyCourseConnect> connects = query.selectFrom(qFullAndDailyCourseConnect)
                .where(
                        qFullAndDailyCourseConnect.fullCourse.fullCourseId.eq(requestDTO.get(0).getFullCourseId())
                                .and(qFullAndDailyCourseConnect.dailyNum.in(dayNums))
                ).fetch();

        // memberId 미리 조회 (불필요한 조회 방지)
        Member myMember = query.selectFrom(qMember)
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        // 모든 dailyCourse를 미리 조회
        Map<Long, DailyCourse> dailyCourseMap = query.selectFrom(qDailyCourse)
                .where(qDailyCourse.dailyCourseId.in(dailyCourseIds))
                .fetch().stream().collect(Collectors.toMap(DailyCourse::getDailyCourseId, Function.identity()));

        // 수정 및 생성할 연결 객체들을 저장할 리스트
        List<FullAndDailyCourseConnect> batchData = new ArrayList<>();
        List<Long> deleteConnectIds = new ArrayList<>();

        // Dto의 데이터와 연결 객체 비교 및 수정/생성
        for (FullCourseModifyDto dto : requestDTO) {
            Long fid = dto.getFullCourseId();
            Long did = dto.getDailyCourseId();
            Long mid = dto.getMemberId();
            Integer dnm = dto.getDayNum();

            // 해당하는 dailyCourse 가져오기
            DailyCourse dailyCourse = dailyCourseMap.get(did);

            boolean found = false;

            // 1. 내 memberId와 다른 경우 -> dayNum이 동일한 기존 연결 객체 삭제 후 새로 생성
            if (!mid.equals(memberId)) {
                for (FullAndDailyCourseConnect conn : connects) {
                    if (conn.getDailyNum().equals(dnm)) {
                        deleteConnectIds.add(conn.getFullDailyCourseConnectId()); // 삭제할 ID 수집
                        break;
                    }
                }
                FullAndDailyCourseConnect newConnect = new FullAndDailyCourseConnect();
                newConnect.createNewConnect(myMember, fullCourse, dailyCourse, dnm); // 내 사용자 ID로 생성
                batchData.add(newConnect); // 새로 생성된 연결 객체 추가
            }
            // 2. 내 memberId와 dayNum이 모두 같은 경우 -> dailyCourseId만 수정
            else {
                for (FullAndDailyCourseConnect conn : connects) {
                    if (conn.getDailyNum().equals(dnm) && conn.getFullCourse().getMember().getMemberId().equals(memberId)) {
                        conn.setDailyCourse(dailyCourse); // dailyCourseId 변경
                        batchData.add(conn); // 변경된 데이터를 batchData에 추가
                        found = true;
                        break;
                    }
                }
            }
        }

        // 삭제할 연결 객체 삭제
        if (!deleteConnectIds.isEmpty()) {
            query.delete(qFullAndDailyCourseConnect)
                    .where(qFullAndDailyCourseConnect.fullDailyCourseConnectId.in(deleteConnectIds))
                    .execute();
        }

        // batchData에 변경된 객체들을 저장
        fullAndDailyCourseConnectRepository.saveAll(batchData);

        // 최종적으로 변경된 FullCourse 반환
        return query.selectFrom(qFullCourse)
                .where(qFullCourse.fullCourseId.eq(fullCourse.getFullCourseId()))
                .fetchOne();
    }






    /**좋아요 설정*/
    public void setFavorite(Long fullCourseId) {

        //수정 필요
        Long memberId = 1L;

        QMember qMember = QMember.member;
        QFullCourse qFullCourse = QFullCourse.fullCourse;
        QFullCourseFavorite qFullCourseFavorite = QFullCourseFavorite.fullCourseFavorite;

        Tuple tuple = query.select(qMember, qFullCourse, qFullCourseFavorite)
                .from(qMember)
                .join(qFullCourse).on(qFullCourse.fullCourseId.eq(fullCourseId))
                .leftJoin(qFullCourseFavorite).on(qFullCourseFavorite.fullCourse.fullCourseId.eq(fullCourseId)
                        .and(qFullCourseFavorite.member.memberId.eq(memberId)))
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        if (tuple == null || tuple.get(qMember) == null || tuple.get(qFullCourse) == null) {
            throw new AddReviewException("Member or FullCourse not found");
        }

        if(tuple.get(qFullCourseFavorite) != null){
            throw new BadRequestException("Already added to favorite");
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
    public void setScrap(Long fullCourseId) {
        //수정필요
        Long memberId = 1L;
        QMember qMember = QMember.member;
        QFullCourse qFullCourse = QFullCourse.fullCourse;
        QFullCourseScrap qFullCourseScrap = QFullCourseScrap.fullCourseScrap;

        Tuple tuple = query.select(qMember, qFullCourse, qFullCourseScrap)
                .from(qMember)
                .join(qFullCourse).on(qFullCourse.fullCourseId.eq(fullCourseId))
                .leftJoin(qFullCourseScrap).on(qFullCourseScrap.fullCourse.fullCourseId.eq(fullCourseId)
                        .and(qFullCourse.member.memberId.eq(memberId)))
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        if (tuple == null || tuple.get(qMember) == null || tuple.get(qFullCourse) == null) {
            throw new AddReviewException("Member or FullCourse not found");
        }

        if(tuple.get(qFullCourseScrap) != null){
            throw new BadRequestException("Already scraped");
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
