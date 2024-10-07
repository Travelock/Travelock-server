package com.travelock.server.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.client.SmallBlockSearchClient;
import com.travelock.server.domain.MiddleBlock;
import com.travelock.server.domain.QSmallBlock;
import com.travelock.server.domain.SmallBlock;
import com.travelock.server.dto.MiddleBlockDTO;
import com.travelock.server.dto.SearchResponseDTO;
import com.travelock.server.dto.SmallBlockRequestDTO;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.repository.MiddleBlockRepository;
import com.travelock.server.repository.SmallBlockRepository;
import com.travelock.server.service.MiddleBlockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmallBlockService {

    private final JPAQueryFactory queryFactory;
    private final SmallBlockRepository smallBlockRepository;
    private final MiddleBlockRepository middleBlockRepository;
    private final SmallBlockSearchClient smallBlockSearchClient;
    private final MiddleBlockService middleBlockService;

    public List<SearchResponseDTO> searchSmallBlockByKeyword(String keyword) {
        return smallBlockSearchClient.searchSmallBlockByKeyword(keyword);
    }

    // 사용자가 선택한 장소를 DB에 저장 (코스 확정 시 호출)
    @Transactional
    public SmallBlock confirmAndCreateSmallBlock(SmallBlockRequestDTO requestDTO) {
        log.info("SmallBlock 확정 및 저장 로직 호출");

        // 미들블록 조회
        MiddleBlockDTO middleBlockDTO = middleBlockService.findMiddleBlockByCategoryCodeAndName(
                requestDTO.getCategoryCode(), requestDTO.getCategoryName());

        // MiddleBlockDTO에서 MiddleBlock 엔티티로 변환 (필요하다면 MiddleBlockRepository로 조회)
        MiddleBlock middleBlock = middleBlockRepository.findById(middleBlockDTO.getMiddleBlockId())
                .orElseThrow(() -> new ResourceNotFoundException("MiddleBlock not found with id: " + middleBlockDTO.getMiddleBlockId()));

        // QueryDSL로 스몰블록 조회
        QSmallBlock qSmallBlock = QSmallBlock.smallBlock;
        SmallBlock smallBlock = queryFactory
                .selectFrom(qSmallBlock)
                .where(qSmallBlock.placeId.eq(requestDTO.getPlaceId()))
                .fetchOne();

        // 스몰블록이 없으면 새로 생성
        if (smallBlock == null) {
            smallBlock = new SmallBlock();
            smallBlock.setSmallBlockData(middleBlock,
                    requestDTO.getPlaceId(),
                    requestDTO.getPlaceName(),
                    requestDTO.getMapX(),
                    requestDTO.getMapY(),
                    requestDTO.getUrl());
            smallBlock = smallBlockRepository.save(smallBlock);
        } else {
            // 스몰블록이 있으면 레퍼 카운트 증가
            smallBlock.incrementReferenceCount();
            smallBlockRepository.save(smallBlock);
        }

        log.info("SmallBlock 저장 완료: {}", smallBlock.getSmallBlockId());
        return smallBlock;
    }

    // 전체 스몰블록 조회
    public List<SmallBlock> getAllSmallBlocks() {
        log.info("모든 SmallBlocks 호출");

        QSmallBlock qSmallBlock = QSmallBlock.smallBlock;
        return queryFactory
                .selectFrom(qSmallBlock)
                .fetch();
    }

    // 특정 스몰블록 조회 (id로)
    public SmallBlock getSmallBlockById(Long id) {
        log.info("특정 SmallBlock 호출, id = {}", id);

        QSmallBlock qSmallBlock = QSmallBlock.smallBlock;
        SmallBlock smallBlock = queryFactory
                .selectFrom(qSmallBlock)
                .where(qSmallBlock.smallBlockId.eq(id))
                .fetchOne();

        if (smallBlock == null) {
            throw new ResourceNotFoundException("SmallBlock not found with id: " + id);
        }

        return smallBlock;
    }
}
