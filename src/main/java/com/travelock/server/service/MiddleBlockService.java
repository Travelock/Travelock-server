package com.travelock.server.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.converter.DTOConverter;
import com.travelock.server.domain.MiddleBlock;
import com.travelock.server.domain.QMiddleBlock;
import com.travelock.server.dto.MiddleBlockDTO;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class MiddleBlockService {
    private final JPAQueryFactory queryFactory;

    public List<MiddleBlockDTO> getAllCategories() {
        log.info("모든 카테고리 조회");
        QMiddleBlock qMiddleBlock = QMiddleBlock.middleBlock;

        List<MiddleBlock> categories = queryFactory
                .selectFrom(qMiddleBlock)
                .fetch();

        return DTOConverter.toDtoList(categories, category -> new MiddleBlockDTO(
                category.getMiddleBlockId(),
                category.getCategoryCode(),
                category.getCategoryName()
        ));
    }

    // 특정 카테고리 코드로 카테고리 조회 (DailyCourse에서 호출)
    public MiddleBlockDTO getCategoryByCode(String categoryCode) {
        log.info("카테고리 조회, categoryCode = {}", categoryCode);

        QMiddleBlock qMiddleBlock = QMiddleBlock.middleBlock;

        MiddleBlock category;

        if (categoryCode == null || categoryCode.isEmpty()) {
            // 카테고리 코드가 null 이거나 빈 경우
            category = queryFactory
                    .selectFrom(qMiddleBlock)
                    .where(qMiddleBlock.categoryName.eq("ETC"))
                    .fetchOne();
            // ETC 표기
        } else {

            category = queryFactory
                    .selectFrom(qMiddleBlock)
                    .where(qMiddleBlock.categoryCode.eq(categoryCode))
                    .fetchOne();
        }

        if (category == null) {
            throw new ResourceNotFoundException("해당 카테고리가 존재하지 않음: " + categoryCode);
        }

        return DTOConverter.toDto(category, middle -> new MiddleBlockDTO(
                middle.getMiddleBlockId(),
                middle.getCategoryCode(),
                middle.getCategoryName()
        ));
    }
}