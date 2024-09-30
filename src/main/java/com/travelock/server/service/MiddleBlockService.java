package com.travelock.server.service;

import com.travelock.server.domain.MiddleBlock;
import com.travelock.server.repository.MiddleBlockRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MiddleBlockService {

    private final MiddleBlockRepository middleBlockRepository;

    @PostConstruct
    public void init() {
        List<MiddleBlock> categories = Arrays.asList(
                new MiddleBlock("MT1", "대형마트"),
                new MiddleBlock("CS2", "편의점"),
                new MiddleBlock("PS3", "어린이집, 유치원"),
                new MiddleBlock("SC4", "학교"),
                new MiddleBlock("AC5", "학원"),
                new MiddleBlock("PK6", "주차장"),
                new MiddleBlock("OL7", "주유소, 충전소"),
                new MiddleBlock("SW8", "지하철역"),
                new MiddleBlock("BK9", "은행"),
                new MiddleBlock("CT1", "문화시설"),
                new MiddleBlock("AG2", "중개업소"),
                new MiddleBlock("PO3", "공공기관"),
                new MiddleBlock("AT4", "관광명소"),
                new MiddleBlock("AD5", "숙박"),
                new MiddleBlock("FD6", "음식점"),
                new MiddleBlock("CE7", "카페"),
                new MiddleBlock("HP8", "병원"),
                new MiddleBlock("PM9", "약국")
        );

        // 카테고리 데이터 저장
        for (MiddleBlock category : categories) {
            middleBlockRepository.findByCategoryCode(category.getCategoryCode())
                    .orElseGet(() -> middleBlockRepository.save(category));

        }
    }

    public List<MiddleBlock> getAllCategories() {
        return middleBlockRepository.findAll();
    }
}
