package com.travelock.server.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.*;
import com.travelock.server.dto.course.daily_create.DailyCourseCreateDto;
import com.travelock.server.dto.course.daily_create.FullBlockDto;
import com.travelock.server.dto.course.daily_create.SmallBlockDto;
import com.travelock.server.exception.base_exceptions.BadRequestException;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.exception.course.AddDailyCourseFavoriteException;
import com.travelock.server.exception.course.AddDailyCourseScrapException;
import com.travelock.server.exception.review.AddReviewException;
import com.travelock.server.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyCourseService {
    private final JPAQueryFactory query;
    private final DailyCourseRepository dailyCourseRepository;
    private final DailyCourseFavoriteRepository dailyCourseFavoriteRepository;
    private final DailyCourseScrapRepository dailyCourseScrapRepository;
    private final FullAndDailyCourseConnectRepository fullAndDailyCourseConnectRepository;
    private final FullBlockRepository fullBlockRepository;


    /**
     * 일자별 일정 조회 By DailyCourseId
     */
    public DailyCourse findDailyCourse(Long dailyCourseId) {
        QDailyCourse qDailyCourse = QDailyCourse.dailyCourse;
        DailyCourse dailyCourse = query
                .selectFrom(qDailyCourse)
                .where(qDailyCourse.dailyCourseId.eq(dailyCourseId))
                .fetchOne(); // 데이터가 없으면 빈리스트 반환

        if (dailyCourse == null) {
            throw new ResourceNotFoundException("Full Course not found by ID("+dailyCourseId+")");
        }

        return dailyCourse;
    }

    /**
     * 일자별 일정 생성
     * - 프론트에서 일일일정 확정시 저장됨.
     */
    @Transactional
    public DailyCourse saveDailyCourse(DailyCourseCreateDto createDto){


//        json 데이터 입력 예
//        DailyCourse = {
//                FullBlock = [
//                    {FID=1,  block_num, BID=1, MB={ID, place_name, place_id}, SB={ID, map_x, map+y, link_url, reference_count }},
//                    {FID=2,  block_num,  BID=2, MB={ID, place_name, place_id,}, SB={ID, map_x, map+y, link_url, reference_count }},
//                    {FID=3,  block_num,  BID=3, MB={ID, place_name, place_id }, SB={ID, map_x, map+y, link_url, reference_count }},
//                    {FID=4,  block_num,  BID=4, MB={ID, place_name, place_id}, SB={ID, map_x, map+y, link_url, reference_count }},
//                    {FID=5,  block_num, BID=5, MB={ID, place_name, place_id}, SB={ID, map_x, map+y, link_url, reference_count }},
//                ]
//        }

        if(createDto == null){
            throw new BadRequestException("createDto is null");
        }

        DailyCourse dailyCourse = new DailyCourse();
        QMember qMember = QMember.member;
        QBigBlock qBigBlock = QBigBlock.bigBlock;
        QMiddleBlock qMiddleBlock = QMiddleBlock.middleBlock;
        QSmallBlock qSmallBlock = QSmallBlock.smallBlock;
        QFullCourse qFullCourse = QFullCourse.fullCourse;

        //Map으로 중복순회 방지
        Map<Long, BigBlock> bigBlockMap = new HashMap<>();
        Map<Long, MiddleBlock> middleBlockMap = new HashMap<>();
        Map<String, SmallBlock> existingSmallBlockMap = new HashMap<>();

        List<FullBlockDto> fullBlockDtoList = createDto.getFullBlockDtoList();
        List<Long> bigBlockIdList = new ArrayList<>();
        List<Long> middleBlockIdList = new ArrayList<>();
        List<String> smaillBlockPlaceIdList = new ArrayList<>();
        List<FullBlock> fullBlocksToBatchSave = new ArrayList<>();

        FullCourse fullCourse = new FullCourse();
        Member member = new Member();

        // bigBlockId와 middleBlockId, smallBlock의 placeId를 각각 리스트에 추가
        for (FullBlockDto dto : fullBlockDtoList) {
            bigBlockIdList.add(dto.getBigBlockId());
            middleBlockIdList.add(dto.getMiddleBlockId());
            smaillBlockPlaceIdList.add(dto.getSmallBlockDto().getPlaceId());
        }

        // @TODO SmallBlock이 새로 생성되는 경우엔 아래 쿼리로 조회 불가 | 테스트는 small Block 값 저장하고 수행 -> 쿼리수정
        // BigBlock과 MiddleBlock, SmallBlock, FullCourse를 조회 --------------------------------------------- DB SELECT(한방쿼리로 필요한 데이터 모두 가져오기)
        List<Tuple> list = query.select(qBigBlock, qMiddleBlock, qSmallBlock)
                .from(qBigBlock)
                .join(qMiddleBlock).on(qMiddleBlock.middleBlockId.in(middleBlockIdList))
                .leftJoin(qSmallBlock).on(qSmallBlock.placeId.in(smaillBlockPlaceIdList)) // LEFT JOIN으로 smallBlock이 없으면 null
                .where(qBigBlock.bigBlockId.in(bigBlockIdList))
                .fetch();


        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No matching data.");
        }

        Tuple firstTuple = list.get(0);
        member = firstTuple.get(qMember);
        fullCourse = firstTuple.get(qFullCourse);

        if (member == null) {
            throw new ResourceNotFoundException("Member not found.");
        }

        if (fullCourse == null) {
            throw new ResourceNotFoundException("FullCourse not found.");
        }

        // 조회된 BigBlock과 MiddleBlock, 이미 존재하는 SmallBlock 객체를 리스트에 추가
        for (Tuple tuple : list) {

            BigBlock bigBlock = tuple.get(qBigBlock);
            MiddleBlock middleBlock = tuple.get(qMiddleBlock);
            SmallBlock smallBlock = tuple.get(qSmallBlock);

            bigBlockMap.put(bigBlock.getBigBlockId(), bigBlock);
            middleBlockMap.put(middleBlock.getMiddleBlockId(), middleBlock);

            if (smallBlock != null) {
                // SmallBlock이 있을때만 처리
                existingSmallBlockMap.put(smallBlock.getPlaceId(), smallBlock);
            }
        }



        for (FullBlockDto fullBlockDto : fullBlockDtoList) {
            // FullBlock과 관련된 엔티티 생성 및 연관 설정
            FullBlock fullBlock = new FullBlock();
            SmallBlockDto smallBlockDto = fullBlockDto.getSmallBlockDto();

            SmallBlock smallBlock = existingSmallBlockMap.get(smallBlockDto.getPlaceId());

            // 존재하지 않으면 새로운 SmallBlock 생성
            if (smallBlock == null) {
                smallBlock = new SmallBlock();

                MiddleBlock middleBlock = middleBlockMap.get(fullBlockDto.getMiddleBlockId());

                if (middleBlock == null) {
                    throw new ResourceNotFoundException("MiddleBlock not found");
                }

                // SmallBlock 엔티티 설정
                smallBlock.createNewSmallBlock(
                        smallBlockDto.getMapX(),
                        smallBlockDto.getMapY(),
                        smallBlockDto.getPlaceId(),
                        middleBlock
                );

                existingSmallBlockMap.put(smallBlock.getPlaceId(), smallBlock);
            }

            BigBlock bigBlock = bigBlockMap.get(fullBlockDto.getBigBlockId());

            if (bigBlock == null) {
                throw new ResourceNotFoundException("BigBlock not found");
            }

            fullBlock.newFullBlock(
                    bigBlock,
                    smallBlock.getMiddleBlock(),
                    smallBlock
            );

            fullBlocksToBatchSave.add(fullBlock);
        }


        //=====================이 아래로 확인 필요...

        //연결객체 생성
        // @TODO daily_course_id null > INSERT 수행 순서 : daily도 저장한 후 id가져와야됨 > 확인 해주세요 (아래도 있습니다)
        //FullAndDailyCourseConnect connect = new FullAndDailyCourseConnect();
        //connect.createNewConnect(member, fullCourse, createDto.getDayNum());

        // DailyCourse 설정 및 저장
        dailyCourse.addDailyCourse(
            member
        );

        // @TODO Full block - Daily Connect에 저장이 안됩니다
        //FullBlock Batch 저장 ----------------------------------------------------------------------- DB INSERT ( 1 )
        fullBlockRepository.saveAll(fullBlocksToBatchSave);
        // Daily Course 저장 ------------------------------------------------------------------------- DB INSERT ( 1 )
        DailyCourse savedDailyCourse = dailyCourseRepository.save(dailyCourse);
        //연결객체 저장 -------------------------------------------------------------------------------- DB INSERT ( 1 )
        // @TODO daily_course_id null > INSERT 수행 순서 변경 필요해서 수정해두었습니다. 확인부탁드려요 -> 확인
        FullAndDailyCourseConnect connect = new FullAndDailyCourseConnect();
        connect.createNewConnect(member, fullCourse, savedDailyCourse, createDto.getDayNum());
        // Daily Course 저장 ------------------------------------------------------------------------- DB INSERT ( 1 )
        fullAndDailyCourseConnectRepository.save(connect);

        return savedDailyCourse;
    }


    /** 일일일정 수정*/
    public DailyCourse modifyDailyCourse(DailyCourseCreateDto request) {

        return null;
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

