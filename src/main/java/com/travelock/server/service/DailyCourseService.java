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

//        현재 사용자 조회 ------------------------------------------------------------------------------------ DB SELECT (아래 select로 병합)
//        Member member = memberRepository.findById(createDto.getMemberId())
//                .orElseThrow(() -> new UsernameNotFoundException("Member not Found"));

        // 초기화
//        List<BigBlock> bigBlocks = new ArrayList<>();
//        List<MiddleBlock> middleBlocks = new ArrayList<>();
//        List<SmallBlock> exsistingSmallBlocks = new ArrayList<>();

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

        // BigBlock과 MiddleBlock, SmallBlock, FullCourse를 조회 --------------------------------------------- DB SELECT(한방쿼리로 필요한 데이터 모두 가져오기)
        List<Tuple> list = query.select(qMember, qBigBlock, qMiddleBlock, qSmallBlock, qFullCourse)
                .from(qMember, qBigBlock, qMiddleBlock, qSmallBlock, qFullCourse)
                .where(
                        qMember.memberId.eq(createDto.getMemberId()),
                        qBigBlock.bigBlockId.in(bigBlockIdList),
                        qMiddleBlock.middleBlockId.in(middleBlockIdList),
                        qSmallBlock.placeId.in(smaillBlockPlaceIdList),
                        qFullCourse.fullCourseId.eq(createDto.getFullCourseId())
                ).fetch();


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

//            bigBlocks.add(bigBlock);
//            middleBlocks.add(middleBlock);
            //이미 존재하는 SmallBlock 리스트
//            exsistingSmallBlocks.add(smallBlock);

            bigBlockMap.put(bigBlock.getBigBlockId(), bigBlock);
            middleBlockMap.put(middleBlock.getMiddleBlockId(), middleBlock);
            existingSmallBlockMap.put(smallBlock.getPlaceId(), smallBlock);
        }



        for (FullBlockDto fullBlockDto : fullBlockDtoList) {
            // FullBlock과 관련된 엔티티 생성 및 연관 설정
            FullBlock fullBlock = new FullBlock();
            SmallBlockDto smallBlockDto = fullBlockDto.getSmallBlockDto();

            // 존재하는 SmallBlock을 확인
//            SmallBlock smallBlock = exsistingSmallBlocks.stream()
//                    .filter(sb -> sb.getPlaceId().equals(smallBlockDto.getPlaceId()))
//                    .findFirst()
//                    .orElse(null);


            // Map을 이용하여 존재하는 SmallBlock을 확인
            SmallBlock smallBlock = existingSmallBlockMap.get(smallBlockDto.getPlaceId());

            // 존재하지 않으면 새로운 SmallBlock 생성
            if (smallBlock == null) {
                smallBlock = new SmallBlock();

//                각 루프마다 미들블록 리스트 순회
//                MiddleBlock middleBlock = middleBlocks.stream()
//                        .filter(m -> m.getMiddleBlockId().equals(fullBlockDto.getMiddleBlockId()))
//                        .findFirst()
//                        .orElseThrow(() -> new RuntimeException("MiddleBlock not found"));

                //Map 사용
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

                // 새로 생성된 SmallBlock을 리스트에 추가하여 중복 방지
//                exsistingSmallBlocks.add(smallBlock);

                // 새로 생성된 SmallBlock을 Map에 추가
                existingSmallBlockMap.put(smallBlock.getPlaceId(), smallBlock);
            }


//            각 루프마다 빅블록 리스트 순회
//            BigBlock bigBlock = bigBlocks.stream()
//                    .filter(b -> b.getBigBlockId().equals(fullBlockDto.getBigBlockId()))
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("BigBlock not found"));// 연관된 BigBlock 객체 설정

            //Map사용
            BigBlock bigBlock = bigBlockMap.get(fullBlockDto.getBigBlockId());

            if (bigBlock == null) {
                throw new ResourceNotFoundException("BigBlock not found");
            }

            // FullBlock객체 생성
            fullBlock.newFullBlock(
                    bigBlock,
                    smallBlock.getMiddleBlock(),
                    smallBlock
            );

            fullBlocksToBatchSave.add(fullBlock);

//            //Full Block 저장 ----------------------------------------------------------------------- DB INSERT ( * n ) -> (Batch INSERT처리로 DB와 통신 1번으로 줄임)
//            fullBlockRepository.save(fullBlock);
        }


        //연결객체 생성
        FullAndDailyCourseConnect connect = new FullAndDailyCourseConnect();
        connect.createNewConnect(member, fullCourse, createDto.getDayNum());

        // DailyCourse 설정 및 저장
        dailyCourse.addDailyCourse(
            member
        );

        //FullBlock Batch 저장 ----------------------------------------------------------------------- DB INSERT ( 1 )
        fullBlockRepository.saveAll(fullBlocksToBatchSave);
        //연결객체 저장 -------------------------------------------------------------------------------- DB INSERT ( 1 )
        fullAndDailyCourseConnectRepository.save(connect);
        // Daily Course 저장 ------------------------------------------------------------------------- DB INSERT ( 1 )
        return dailyCourseRepository.save(dailyCourse);

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

