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

    // 키워드 없을 경우 예외처리 추가하기  // API 키워드 조회
    public List<SearchResponseDTO> searchSmallBlockByKeyword(String keyword) {
        return smallBlockSearchClient.searchSmallBlockByKeyword(keyword);
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
