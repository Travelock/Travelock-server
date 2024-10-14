package com.travelock.server.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.*;
import com.travelock.server.dto.block.FullBlockRequestDTO;
import com.travelock.server.dto.course.daily.DailyCourseRequestDTO;
import com.travelock.server.dto.block.SmallBlockRequestDTO;
import com.travelock.server.exception.base_exceptions.BadRequestException;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.exception.course.AddDailyCourseFavoriteException;
import com.travelock.server.exception.course.AddDailyCourseScrapException;
import com.travelock.server.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DailyCourseService {
    private final JPAQueryFactory query;
    private final DailyCourseRepository dailyCourseRepository;
    private final DailyCourseFavoriteRepository dailyCourseFavoriteRepository;
    private final SmallBlockRepository smallBlockRepository;
    private final DailyBlockConnectRepository dailyBlockConnectRepository;
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
            throw new ResourceNotFoundException("Full Course not found by ID(" + dailyCourseId + ")");
        }

        return dailyCourse;
    }

    /**
     * 일자별 일정 생성
     * - 프론트에서 일일일정 확정시 저장됨.
     */
    public DailyCourse saveDailyCourse(DailyCourseRequestDTO createDto) {

        if (createDto == null) {
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

        List<FullBlockRequestDTO> fullBlockDtoList = createDto.getFullBlockDtoList();
        List<Long> bigBlockIdList = new ArrayList<>();
        List<Long> middleBlockIdList = new ArrayList<>();
        List<String> smaillBlockPlaceIdList = new ArrayList<>();
        List<FullBlock> fullBlocksToBatchSave = new ArrayList<>();
        List<SmallBlock> smallBlocksToBatchSave = new ArrayList<>();
        List<DailyBlockConnect> dailyBlockConnects = new ArrayList<>();

        FullCourse fullCourse = new FullCourse();
        Member member = new Member();

        // bigBlockId와 middleBlockId, smallBlock의 placeId를 각각 리스트에 추가
        for (FullBlockRequestDTO dto : fullBlockDtoList) {
            bigBlockIdList.add(dto.getBigBlockId());
            middleBlockIdList.add(dto.getMiddleBlockId());
            smaillBlockPlaceIdList.add(dto.getSmallBlockDto().getPlaceId());
        }

        // BigBlock과 MiddleBlock, SmallBlock, FullCourse, Member를 조회 ---- DB SELECT(한방쿼리로 필요한 데이터 모두 가져오기)
        List<Tuple> list = query.select(qBigBlock, qMiddleBlock, qSmallBlock, qMember, qFullCourse)
                .from(qBigBlock)
                .join(qMember).on(qMember.memberId.eq(createDto.getMemberId()))
                .join(qFullCourse).on(qFullCourse.fullCourseId.eq(createDto.getFullCourseId()))
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


        for (FullBlockRequestDTO fullBlockRequestDto : fullBlockDtoList) {
            // FullBlock과 관련된 엔티티 생성 및 연관 설정
            FullBlock fullBlock = new FullBlock();
            SmallBlockRequestDTO smallBlockRequestDTO = fullBlockRequestDto.getSmallBlockDto();

            SmallBlock smallBlock = existingSmallBlockMap.get(smallBlockRequestDTO.getPlaceId());

            BigBlock bigBlock = bigBlockMap.get(fullBlockRequestDto.getBigBlockId());

            if (bigBlock == null) {
                throw new ResourceNotFoundException("BigBlock not found");
            }

            // 존재하지 않으면 새로운 SmallBlock 생성
            if (smallBlock == null) {
                smallBlock = new SmallBlock();

                MiddleBlock middleBlock = middleBlockMap.get(fullBlockRequestDto.getMiddleBlockId());

                if (middleBlock == null) {
                    throw new ResourceNotFoundException("MiddleBlock not found");
                }


                // SmallBlock 엔티티 설정
                smallBlock.createNewSmallBlock(
                        smallBlockRequestDTO.getMapX(),
                        smallBlockRequestDTO.getMapY(),
                        smallBlockRequestDTO.getPlaceId(),
                        smallBlockRequestDTO.getPlaceName(),
                        middleBlock,
                        bigBlock
                );

                //새로 생성된 SmallBlock객체는 저장목록에 추가
                smallBlocksToBatchSave.add(smallBlock);

                existingSmallBlockMap.put(smallBlock.getPlaceId(), smallBlock);
            }



            fullBlock.newFullBlock(
                    bigBlock,
                    smallBlock.getMiddleBlock(),
                    smallBlock
            );

            fullBlocksToBatchSave.add(fullBlock);
        }


        // DailyCourse 설정
        dailyCourse.addDailyCourse(
                member
        );


        //SmallBlock Batch 저장 ---------------------------------------------------------------------- DB INSERT ( 1 )
        smallBlockRepository.saveAll(smallBlocksToBatchSave);

        //FullBlock Batch 저장 ----------------------------------------------------------------------- DB INSERT ( 1 )
        List<FullBlock> fullBlocks = fullBlockRepository.saveAll(fullBlocksToBatchSave);

        // Daily Course 저장 ------------------------------------------------------------------------- DB INSERT ( 1 )
        DailyCourse savedDailyCourse = dailyCourseRepository.save(dailyCourse);

        //FullAndDaily연결객체 저장 -------------------------------------------------------------------- DB INSERT ( 1 )
        FullAndDailyCourseConnect connect = new FullAndDailyCourseConnect();
        connect.createNewConnect(member, fullCourse, savedDailyCourse, createDto.getDayNum());
        fullAndDailyCourseConnectRepository.save(connect);


        //DailyBlockConnect 목록 생성
        for (FullBlockRequestDTO fbt : fullBlockDtoList) {
            DailyBlockConnect tmp = new DailyBlockConnect();

            //저장된 FullBlock 객체들에서 선택
            for (FullBlock fb : fullBlocks) {

                //요청된 dto의 fullBlock데이터와 저장된 fullBlock의 데이터가 모두 같으면 dailyBlock연결객체에 추가.
                if (
                        fb.getBigBlock().getBigBlockId() == fbt.getBigBlockId() &&
                                fb.getMiddleBlock().getMiddleBlockId() == fbt.getMiddleBlockId() &&
                                fb.getSmallBlock().getPlaceId().equals(fbt.getSmallBlockDto().getPlaceId())
                ) {
                    tmp.newConnect(
                            fbt.getBlockNum(),
                            savedDailyCourse,
                            fb
                    );

                    dailyBlockConnects.add(tmp);
                }
            }
        }

        //DailyBlock연결객체 저장 ---------------------------------------------------------------------- DB INSERT ( 1 )
        dailyBlockConnectRepository.saveAll(dailyBlockConnects);


        return savedDailyCourse;
    }


    /**
     * 일일일정 수정
     */
    public DailyCourse modifyDailyCourse(DailyCourseRequestDTO request) {


        DailyCourse dailyCourse = new DailyCourse();
        QBigBlock qBigBlock = QBigBlock.bigBlock;
        QFullBlock qFullBlock = QFullBlock.fullBlock;
        QMiddleBlock qMiddleBlock = QMiddleBlock.middleBlock;
        QSmallBlock qSmallBlock = QSmallBlock.smallBlock;
        QDailyCourse qDailyCourse = QDailyCourse.dailyCourse;
        QDailyBlockConnect qDailyBlockConnect = QDailyBlockConnect.dailyBlockConnect;

        //Map으로 중복순회 방지
        Map<Long, BigBlock> bigBlockMap = new HashMap<>();
        Map<Long, MiddleBlock> middleBlockMap = new HashMap<>();
        Map<String, SmallBlock> smallBlockMap = new HashMap<>();
        Set<String> smaillBlockPlaceIdSet = new HashSet<>();

        List<Long> bigBlockIdList = new ArrayList<>();
        List<Long> fullBlockIdList = new ArrayList<>();
        List<Long> middleBlockIdList = new ArrayList<>();
        List<String> smallBlockPlaceIdList = new ArrayList<>();
        List<FullBlock> savedFullBlocks = new ArrayList<>();
        List<DailyBlockConnect> dailyBlockConnects = new ArrayList<>();
        List<FullBlockRequestDTO> reqFullBlockDtos = request.getFullBlockDtoList();

        List<DailyCourseModifyTemp> modifyTemps = new ArrayList<>();
        List<Long> befConnectIds = new ArrayList<>();



        for(FullBlockRequestDTO dto : reqFullBlockDtos){
            bigBlockIdList.add(dto.getBigBlockId());
            middleBlockIdList.add(dto.getMiddleBlockId());
            smallBlockPlaceIdList.add(dto.getSmallBlockDto().getPlaceId());
        }
        //--------------------------------------------------------SELECT(1)
        List<Tuple> tuples = query.select(qDailyCourse, qDailyBlockConnect, qFullBlock, qBigBlock, qMiddleBlock, qSmallBlock)
                .from(qDailyBlockConnect)
                .join(qDailyCourse).on(qDailyCourse.dailyCourseId.eq(request.getDailyCourseId()).and(qDailyBlockConnect.dailyCourse.dailyCourseId.eq(qDailyCourse.dailyCourseId)))
                .join(qFullBlock).on(qFullBlock.fullBlockId.eq(qDailyBlockConnect.fullBlock.fullBlockId))
                .join(qBigBlock).on(qBigBlock.bigBlockId.in(bigBlockIdList).and(qBigBlock.bigBlockId.eq(qFullBlock.bigBlock.bigBlockId)))
                .join(qMiddleBlock).on(qMiddleBlock.middleBlockId.in(middleBlockIdList).and(qMiddleBlock.middleBlockId.eq(qFullBlock.middleBlock.middleBlockId)))
                .leftJoin(qSmallBlock).on(qSmallBlock.placeId.in(smallBlockPlaceIdList))
                .where(qDailyBlockConnect.dailyCourse.dailyCourseId.eq(request.getDailyCourseId()))
                .fetch();

        if(tuples == null){
            throw new ResourceNotFoundException("No match data");
        }

        Tuple firstTuple = tuples.get(0);
        dailyCourse = firstTuple.get(qDailyCourse);

        if(dailyCourse == null){
            throw new ResourceNotFoundException("DailyCourse not found");
        }

        for(Tuple tuple : tuples){
            //map으로 저장 필요
            DailyBlockConnect dailyBlockConnect = tuple.get(qDailyBlockConnect);
            FullBlock fullBlock = tuple.get(qFullBlock);
            BigBlock bigBlock = tuple.get(qBigBlock);
            MiddleBlock middleBlock = tuple.get(qMiddleBlock);
            SmallBlock smallBlock = fullBlock.getSmallBlock();



            dailyBlockConnects.add(dailyBlockConnect);
            savedFullBlocks.add(fullBlock);

            fullBlockIdList.add(tuple.get(qDailyBlockConnect).getFullBlock().getFullBlockId());
            befConnectIds.add(dailyBlockConnect.getDailyBlockConnectId());


            bigBlockMap.put(bigBlock.getBigBlockId(), bigBlock);
            middleBlockMap.put(middleBlock.getMiddleBlockId(), middleBlock);
            smallBlockMap.put(smallBlock.getPlaceId(), smallBlock);
        }


        //저장된 FullBlock에서 저장된 SmallBlock 조회
        for(FullBlock f : savedFullBlocks){
            smaillBlockPlaceIdSet.add(f.getSmallBlock().getPlaceId());
        }

        reqFullBlockDtos = request.getFullBlockDtoList();

        List<String> reqSmallIds = new ArrayList<>();
        List<SmallBlock> reqSmallBlocks = new ArrayList<>();

        for(FullBlockRequestDTO req : reqFullBlockDtos){
            reqSmallIds.add(req.getSmallBlockDto().getPlaceId());
        }

        reqSmallIds = reqSmallIds.stream()
                .filter(id -> !smaillBlockPlaceIdSet.contains(id))  // smaillBlockPlaceIdSet에 없는 id만 남김
                .collect(Collectors.toList());

        //남은 placeid로 새로운 스몰블럭 저장
        List<FullBlock> batchFullBlocks = new ArrayList<>();


        for (FullBlockRequestDTO req : reqFullBlockDtos) {
            // SmallBlock 생성 (필요한 경우에만)
            SmallBlock smallBlock;
            if (reqSmallIds.contains(req.getSmallBlockDto().getPlaceId())) {
                // 새로운 SmallBlock 생성
                smallBlock = new SmallBlock();
                smallBlock.createNewSmallBlock(
                        req.getSmallBlockDto().getMapX(),
                        req.getSmallBlockDto().getMapY(),
                        req.getSmallBlockDto().getPlaceId(),
                        req.getSmallBlockDto().getPlaceName(),
                        // req의 middleBlockId로 map 검색
                        middleBlockMap.get(req.getMiddleBlockId()),
                        bigBlockMap.get(req.getBigBlockId())
                );
                reqSmallBlocks.add(smallBlock);
            } else {
                // 기존 SmallBlock 에서 map 조회
                smallBlock = smallBlockMap.get(req.getSmallBlockDto().getPlaceId());
            }

            // FullBlock 생성
            FullBlock fullBlock = new FullBlock();
            fullBlock.newFullBlock(
                    //map에서 조회
                    bigBlockMap.get(req.getBigBlockId()),
                    middleBlockMap.get(req.getMiddleBlockId()),
                    smallBlock // 위에서 생성되거나 조회된 SmallBlock 사용
            );

            // FullBlock 리스트에 추가
            batchFullBlocks.add(fullBlock);

            DailyCourseModifyTemp dtmp = new DailyCourseModifyTemp();

            dtmp.fullBlock = fullBlock;
            dtmp.smallBlock = smallBlock;
            dtmp.blockNum = req.getBlockNum();

            modifyTemps.add(dtmp);
        }

        // ----------------------------------------------------------INSERT(1)
        List<SmallBlock> newSmallBlocks = smallBlockRepository.saveAll(reqSmallBlocks);
        // ----------------------------------------------------------INSERT(1)
        List<FullBlock> newFullBlocks = fullBlockRepository.saveAll(batchFullBlocks);

        //풀블럭 일단.. 해결


        //연결객체 생성하면서 blocknum 중복데이터 조회 후 삭제

        List<DailyBlockConnect> batchConnect = new ArrayList<>();
        for(DailyCourseModifyTemp tmp : modifyTemps){
            DailyBlockConnect dbc = new DailyBlockConnect();
            dbc.newConnect(
                    tmp.blockNum,
                    dailyCourse,
                    tmp.fullBlock
            );
            batchConnect.add(dbc);
        }

        // ----------------------------------------------------------INSERT(1)
        List<DailyBlockConnect> newConnects = dailyBlockConnectRepository.saveAll(batchConnect);
        List<Long> befIds = new ArrayList<>();


        // ----------------------------------------------------------DELETE(1)
        long executed = query.delete(qDailyBlockConnect)
                .where(
                        qDailyBlockConnect.dailyBlockConnectId.in(befConnectIds)
                ).execute();

        // ----------------------------------------------------------DELETE(1)
        long execute = query.delete(qFullBlock)
                .where(
                        qFullBlock.fullBlockId.in(fullBlockIdList)
                ).execute();




        // ----------------------------------------------------------SELECT(1)
        return query.selectFrom(qDailyCourse).where(qDailyCourse.dailyCourseId.eq(request.getDailyCourseId())).fetchOne();
    }


    /**
     * 좋아요 설정
     */
    public void setFavorite(Long dailyCourseId) {
        Long memberId = 1L;
        QMember qMember = QMember.member;
        QDailyCourse qDailyCourse = QDailyCourse.dailyCourse;
        QDailyCourseFavorite qDailyCourseFavorite = QDailyCourseFavorite.dailyCourseFavorite;

        Tuple tuple = query.select(qMember, qDailyCourse, qDailyCourseFavorite)
                .from(qMember)
                .join(qDailyCourse).on(qDailyCourse.dailyCourseId.eq(dailyCourseId))
                .leftJoin(qDailyCourseFavorite).on(qDailyCourseFavorite.dailyCourse.dailyCourseId.eq(dailyCourseId)
                        .and(qDailyCourseFavorite.member.memberId.eq(memberId)))
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        if (tuple == null || tuple.get(qMember) == null || tuple.get(qDailyCourse) == null) {
            throw new BadRequestException("Member or DailyCourse not found");
        }

        if (tuple.get(qDailyCourseFavorite) != null) {
            throw new BadRequestException("Already added to favorite");
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

    /**
     * 스크랩 설정
     */
    public void setScrap(Long dailyCourseId) {

        Long memberId = 1L;
        QMember qMember = QMember.member;
        QDailyCourse qDailyCourse = QDailyCourse.dailyCourse;
        QDailyCourseScrap qDailyCourseScrap = QDailyCourseScrap.dailyCourseScrap;

        Tuple tuple = query.select(qMember, qDailyCourse, qDailyCourseScrap)
                .from(qMember)
                .join(qDailyCourse).on(qDailyCourse.dailyCourseId.eq(dailyCourseId))
                .leftJoin(qDailyCourseScrap).on(qDailyCourseScrap.dailyCourse.dailyCourseId.eq(dailyCourseId)
                        .and(qDailyCourseScrap.member.memberId.eq(memberId)))
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        if (tuple == null || tuple.get(qMember) == null || tuple.get(qDailyCourse) == null) {
            throw new BadRequestException("Member or DailyCourse not found");
        }

        if (tuple.get(qDailyCourseScrap) != null) {
            throw new BadRequestException("Already scraped");
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

    /**
     * 좋아요한 일일일정 목록
     */
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

    /**
     * 스크랩한 일일일정 목록
     */
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

class DailyCourseModifyTemp {
    Long connectId;
    Integer blockNum;
    boolean delete;
    FullBlock fullBlock;
    BigBlock bigBlock;
    MiddleBlock middleBlock;
    SmallBlock smallBlock;

}

