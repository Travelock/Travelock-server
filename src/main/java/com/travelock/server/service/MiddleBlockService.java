package com.travelock.server.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.converter.DTOConverter;
import com.travelock.server.domain.MiddleBlock;
import com.travelock.server.domain.QMiddleBlock;
import com.travelock.server.dto.MiddleBlockDTO;
import com.travelock.server.repository.MiddleBlockRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MiddleBlockService {
    private final JPAQueryFactory queryFactory;
    private final MiddleBlockRepository middleBlockRepository;

    public List<MiddleBlockDTO> getAllCategories() {
        log.info("모든 카테고리 조회");
        QMiddleBlock qMiddleBlock = QMiddleBlock.middleBlock;

        // QueryDSL로 카테고리 조회 후 DTO로 변환
        List<MiddleBlock> categories = queryFactory
                .selectFrom(qMiddleBlock)
                .fetch();

        return DTOConverter.toDtoList(categories, category -> new MiddleBlockDTO(
                category.getMiddleBlockId(),
                category.getCategoryCode(),
                category.getCategoryName()

        ));
    }

    // 특정 카테고리 코드로 카테고리 조회
    public MiddleBlockDTO getCategoryByCode(String categoryCode) {
        log.info("카테고리 조회, categoryCode = {}", categoryCode);

        QMiddleBlock qMiddleBlock = QMiddleBlock.middleBlock;

        MiddleBlock category = queryFactory
                .selectFrom(qMiddleBlock)
                .where(qMiddleBlock.categoryCode.eq(categoryCode))
                .fetchOne();

        if (category == null) {
            throw new RuntimeException("해당 카테고리가 존재하지 않습니다.");
        }

        // DTOConverter를 사용해 엔티티를 DTO로 변환
        return DTOConverter.toDto(category, cat -> new MiddleBlockDTO(
                cat.getMiddleBlockId(),
                cat.getCategoryCode(),
                cat.getCategoryName()
        ));
    }


}
