package com.travelock.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.converter.DTOConverter;
import com.travelock.server.domain.MiddleBlock;
import com.travelock.server.domain.QMiddleBlock;
import com.travelock.server.domain.QSmallBlock;
import com.travelock.server.domain.SmallBlock;
import com.travelock.server.dto.SearchResponseDTO;
import com.travelock.server.dto.SmallBlockRequestDTO;
import com.travelock.server.dto.SmallBlockResponseDTO;
import com.travelock.server.repository.MiddleBlockRepository;
import com.travelock.server.repository.SmallBlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmallBlockService {

    private final JPAQueryFactory queryFactory;
    private final SmallBlockRepository smallBlockRepository;
    private final ObjectMapper objectMapper;
    private final MiddleBlockRepository middleBlockRepository;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    // 키워드로 장소 검색 (DB 저장 없음, 프론트에 정보만 제공)
    public List<SearchResponseDTO> searchSmallBlockByKeyword(String keyword) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + keyword;
        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        var entity = new HttpEntity<>(headers);

        // 카카오 API 호출
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        JsonNode documents = jsonNode.get("documents");

        // 검색 결과를 SmallBlockResponseDTO로 변환하여 리스트로 반환
        List<SearchResponseDTO> results = new ArrayList<>();
        for (JsonNode document : documents) {
            SearchResponseDTO dto = new SearchResponseDTO();
            dto.setPlaceId(document.get("id").asText());
            dto.setPlaceName(document.get("place_name").asText());
            dto.setMapX(document.get("x").asText());
            dto.setMapY(document.get("y").asText());
            dto.setCategoryCode(document.get("category_group_code").asText());

            String fullCategoryName = document.get("category_name").asText();
            String parsedCategoryName = fullCategoryName.split(" > ")[0];
            dto.setCategoryName(parsedCategoryName);


            results.add(dto);
        }

        return results;
    }

    // 사용자가 선택한 장소를 DB에 저장 (코스 확정 시 호출)
    @Transactional
    public void confirmAndCreateSmallBlock(SmallBlockRequestDTO requestDTO, Long middleBlockId) throws Exception {
        log.info("SmallBlock 확정 및 저장 로직 호출");

        QMiddleBlock qMiddleBlock = QMiddleBlock.middleBlock;

        // QueryDSL로 MiddleBlock 조회
        MiddleBlock middleBlock = queryFactory
                .selectFrom(qMiddleBlock)
                .where(qMiddleBlock.middleBlockId.eq(middleBlockId))
                .fetchOne();

        if (middleBlock == null) {
            log.error("MiddleBlock not found for id: {}", middleBlockId);
            throw new Exception("MiddleBlock not found");
        }

        // 스몰블록 생성 또는 업데이트
        SmallBlock smallBlock = smallBlockRepository.findByPlaceId(requestDTO.getPlaceId())
                .orElseGet(() -> {
                    SmallBlock newSmallBlock = new SmallBlock();
                    newSmallBlock.setSmallBlockData(middleBlock, requestDTO.getPlaceId(),
                            requestDTO.getPlaceName(), requestDTO.getMapX(), requestDTO.getMapY(), requestDTO.getUrl());
                    return smallBlockRepository.save(newSmallBlock);
                });

        // 이미 존재하는 스몰블록일 경우 레퍼 카운트 증가
        smallBlock.incrementReferenceCount();
        smallBlockRepository.save(smallBlock);

        log.info("SmallBlock 저장 완료: {}", smallBlock.getSmallBlockId());
    }

    // 전체 스몰블록 조회
    public List<SmallBlockResponseDTO> getAllSmallBlocks() {
        log.info("모든 SmallBlocks 호출");

        QSmallBlock qSmallBlock = QSmallBlock.smallBlock;
        List<SmallBlock> smallBlocks = queryFactory
                .selectFrom(qSmallBlock)
                .fetch();

        // DTOConverter를 사용해 엔티티를 DTO로 변환
        return DTOConverter.toDtoList(smallBlocks, smallBlock -> new SmallBlockResponseDTO(
                smallBlock.getSmallBlockId(),
                smallBlock.getPlaceId(),
                smallBlock.getPlaceName(),
                smallBlock.getMapX(),
                smallBlock.getMapY(),
//                smallBlock.getUrl(),
                smallBlock.getReferenceCount()
        ));
    }

    // 특정 스몰블록 조회 (id로)
    public SmallBlockResponseDTO getSmallBlockById(Long id) {
        log.info("특정 SmallBlock 호출, id = {}", id);

        QSmallBlock qSmallBlock = QSmallBlock.smallBlock;
        SmallBlock smallBlock = queryFactory
                .selectFrom(qSmallBlock)
                .where(qSmallBlock.smallBlockId.eq(id))
                .fetchOne();

        if (smallBlock == null) {
            throw new RuntimeException("스몰 블록을 찾을 수 없음.");
        }

        // DTOConverter를 사용해 엔티티를 DTO로 변환
        return DTOConverter.toDto(smallBlock, block -> new SmallBlockResponseDTO(
                block.getSmallBlockId(),
                block.getPlaceId(),
                block.getPlaceName(),
                block.getMapX(),
                block.getMapY(),
//                block.getUrl(),
                block.getReferenceCount()
        ));
    }
}