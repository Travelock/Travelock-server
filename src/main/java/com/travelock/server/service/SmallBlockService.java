package com.travelock.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelock.server.domain.MiddleBlock;
import com.travelock.server.domain.SmallBlock;
import com.travelock.server.repository.MiddleBlockRepository;
import com.travelock.server.repository.SmallBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmallBlockService {

    private final SmallBlockRepository smallBlockRepository;
    // JSON 파싱
    private final ObjectMapper objectMapper;
    private final MiddleBlockRepository middleBlockRepository;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Transactional
    public void searchAndCreateSmallBlock(String keyword, Long middleBlockId) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + keyword;
        var headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        var entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("API 호출 응답: " + response.getBody());
        // 응답 처리
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
//        List<JsonNode> documents = jsonNode.get("documents").findValues("documents");
        JsonNode documents = jsonNode.get("documents");
        // 미들블록 id를 통해 middleblock을 가져옴
        MiddleBlock middleBlock = middleBlockRepository.findById(middleBlockId)
                .orElseThrow(() -> new Exception("MiddleBlock not found"));

        for (JsonNode document : documents) {
            String placeId = document.get("id").asText();
            String placeName = document.get("place_name").asText();
            String mapX = document.get("x").asText();
            String mapY = document.get("y").asText();
            String placeUrl = document.get("place_url").asText();

            // 스몰블록 생성

            SmallBlock smallBlock = smallBlockRepository.findByPlaceId(placeId);
            if (smallBlock == null) {
                smallBlock = new SmallBlock();
                smallBlock.setPlaceId(placeId);
                smallBlock.setPlaceName(placeName);
                smallBlock.setMapX(mapX);
                smallBlock.setMapY(mapY);
                smallBlock.setUrl(placeUrl);
                smallBlock.setMiddleBlock(middleBlock);
                smallBlock.setReferenceCount(1);
                SmallBlock savedBlock = smallBlockRepository.save(smallBlock);

                System.out.println("Saved SmallBlock ID: " + savedBlock.getSmallBlockId());

            } else {
                // 이미 있는 블록이라면, 레퍼카운트 증가
                smallBlock.setReferenceCount(smallBlock.getReferenceCount() + 1);
                smallBlockRepository.save(smallBlock);
            }
        }
    }

    // 전체 스몰블록 조회
    public List<SmallBlock> getAllSmallBlocks() {
        return smallBlockRepository.findAll();
    }

    // 특정 스몰블록 조회 (id로)
    public SmallBlock getSmallBlockById(Long id) {
        Optional<SmallBlock> smallBlock = smallBlockRepository.findById(id);
        return smallBlock.orElseThrow(() -> new RuntimeException("스몰 블록을 찾을 수 없음."));
    }
}
