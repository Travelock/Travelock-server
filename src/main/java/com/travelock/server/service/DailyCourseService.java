package com.travelock.server.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.*;
import com.travelock.server.dto.course.daily_create.DailyCourseCreateDto;
import com.travelock.server.dto.course.daily_create.FullBlockDto;
import com.travelock.server.dto.course.daily_create.SmallBlockDto;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.exception.course.AddDailyCourseFavoriteException;
import com.travelock.server.exception.course.AddDailyCourseScrapException;
import com.travelock.server.exception.review.AddReviewException;
import com.travelock.server.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final FullAndDailyCourseConnectRepository fullAndDailyCourseConnectRepository;
    private final SmallBlockRepository smallBlockRepository;
    private final FullBlockRepository fullBlockRepository;

    /**
     * 일자별 일정 생성
     * - 프론트에서 일일일정 확정시 저장됨.
     */
//    public DailyCourse saveCourse(DailyCourseRequestDTO requestDTO) {
//
//        // 유효성 검사
//        // 필수값 체크
//        if (requestDTO.getFullCourseId() == null || requestDTO.getFullCourseId() == 0
//        || requestDTO.getMemberId() == null || requestDTO.getMemberId() == 0) {
//            // @TODO 일단 내부 확인용도로 응답메시지 작성
//            throw new BadRequestException("fullCourseId or memberId is required");
//        }
//        // 멤버 조회
//        Member member = memberRepository.findById(1L).get(); // 테스트
//        // 전체 일정 조회
//        FullCourse fullCourse = fullCourseService.findFullCourse(requestDTO.getFullCourseId());
//
//        //연결객체 생성
//        FullAndDailyCourseConnect connect = new FullAndDailyCourseConnect();
//        connect.createNewConnect(member.getMemberId(), fullCourse.getFullCourseId(), requestDTO.getDayNum());
//
//        // DB INSERT
//        DailyCourse dailyCourse = new DailyCourse();
//        dailyCourse.addDailyCourse(
//                requestDTO.getDayNum(),
//                member,
//                fullCourse
//        );
//
//        try {
//            //연결객체 저장
//            fullAndDailyCourseConnectRepository.save(connect);
//            //일일일정객체 저장
//            return dailyCourseRepository.save(dailyCourse);
//
//        } catch (Exception e) {
//            // @TODO Add log
//
//            throw new AddReviewException("저장에 실패했습니다." + e.getMessage() );
//        }
//
//    }

    public DailyCourse saveDailyCourse(DailyCourseCreateDto createDto){
        DailyCourse dailyCourse = new DailyCourse();
        QBigBlock qBigBlock = QBigBlock.bigBlock;
        QMiddleBlock qMiddleBlock = QMiddleBlock.middleBlock;
        QFullCourse qFullCourse = QFullCourse.fullCourse;

        Member member = memberRepository.findById(createDto.getMemberId())
                .orElseThrow(() -> new UsernameNotFoundException("Member not Found"));

        // 초기화
        List<BigBlock> bigBlocks = new ArrayList<>();
        List<MiddleBlock> middleBlocks = new ArrayList<>();
        List<FullBlockDto> fullBlockDtoList = createDto.getFullBlockDtoList();
        List<Long> bigBlockIdList = new ArrayList<>();
        List<Long> middleBlockIdList = new ArrayList<>();
        FullCourse fullCourse = new FullCourse();

        // bigBlockId와 middleBlockId를 각각 리스트에 추가
        for (FullBlockDto dto : fullBlockDtoList) {
            bigBlockIdList.add(dto.getBigBlockId());
            middleBlockIdList.add(dto.getMiddleBlockId());
        }

        // QueryDSL을 사용하여 BigBlock과 MiddleBlock을 조회
        List<Tuple> list = query.select(qBigBlock, qMiddleBlock, qFullCourse)
                .from(qBigBlock, qMiddleBlock, qFullCourse)
                .where(
                        qBigBlock.bigBlockId.in(bigBlockIdList),
                        qMiddleBlock.middleBlockId.in(middleBlockIdList),
                        qFullCourse.fullCourseId.eq(createDto.getFullCourseId())
                ).fetch();


        // 조회된 BigBlock과 MiddleBlock 객체를 리스트에 추가
        for (Tuple tuple : list) {
            BigBlock bigBlock = tuple.get(qBigBlock);
            MiddleBlock middleBlock = tuple.get(qMiddleBlock);
            if(tuple.get(qFullCourse) != null) {
                fullCourse = tuple.get(qFullCourse);
            }

            bigBlocks.add(bigBlock);
            middleBlocks.add(middleBlock);
        }


        // 각 FullBlockDto를 순회하면서 FullBlock 및 SmallBlock을 생성, 저장
        for (FullBlockDto fullBlockDto : fullBlockDtoList) {
            // FullBlock과 관련된 엔티티 생성 및 연관 설정
            FullBlock fullBlock = new FullBlock();
            SmallBlockDto smallBlockDto = fullBlockDto.getSmallBlockDto();

            // smallBlockDto의 정보를 기반으로 SmallBlock 생성
            SmallBlock smallBlock = new SmallBlock();
            MiddleBlock middleBlock = middleBlocks.stream()
                    .filter(m -> m.getMiddleBlockId().equals(fullBlockDto.getMiddleBlockId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("MiddleBlock not found"));

            // SmallBlock 엔티티 설정
            smallBlock.createNewSmallBlock(
                    smallBlockDto.getMapX(),
                    smallBlockDto.getMapY(),
                    smallBlockDto.getPlaceId(),
                    middleBlock
            );

            BigBlock bigBlock = bigBlocks.stream()
                    .filter(b -> b.getBigBlockId().equals(fullBlockDto.getBigBlockId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("BigBlock not found"));// 연관된 BigBlock 객체 설정


            // FullBlock객체 생성
            fullBlock.newFullBlock(
                    bigBlock,
                    middleBlock,
                    smallBlock
            );

            //Full Block 저장
            fullBlockRepository.save(fullBlock);
        }

        // DailyCourse 설정 및 저장
        dailyCourse.addDailyCourse(
            createDto.getDayNum(), member, fullCourse
        );
        return dailyCourseRepository.save(dailyCourse);
    }





    /**좋아요 설정*/
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

    /**스크랩 설정*/
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

    /**좋아요한 일일일정 목록*/
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

    /**스크랩한 일일일정 목록*/
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

