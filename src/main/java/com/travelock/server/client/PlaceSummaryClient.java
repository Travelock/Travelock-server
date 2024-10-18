package com.travelock.server.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelock.server.dto.block.PlaceSummaryResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceSummaryClient {

    private final WebClient webClient = WebClient.builder().build();

    // 중복된 도시 이름 목록 (Set을 사용하여 중복 확인)
    private final Set<String> duplicatedCityNames = new HashSet<>(Set.of("중구", "서구", "동구", "남구", "북구"));

    public PlaceSummaryResponseDTO fetchCitySummary(String stateName, String cityName) {
        // 중복된 도시 이름인지 확인하고, 중복된 경우 stateName과 결합
        String searchKeyword;
        if (duplicatedCityNames.contains(cityName)) {
            searchKeyword = cityName + "_(" + stateName + ")";
        } else {
            searchKeyword = cityName;
        }

        String wikipediaApiUrl = "https://ko.wikipedia.org/api/rest_v1/page/summary/" + searchKeyword;
        log.info("PlaceSummaryClient::fetchCitySummary START - searchKeyword: {}", searchKeyword);

        // Wikipedia API 호출
        String jsonString = webClient.get()
                .uri(wikipediaApiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 동기 처리

        // JsonNode에서 필요한 필드 추출
        PlaceSummaryResponseDTO placeSummaryResponseDTO = new PlaceSummaryResponseDTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // 필드 매핑
            placeSummaryResponseDTO.setTitle(jsonNode.has("title") ? jsonNode.get("title").asText() : "No title available");
            placeSummaryResponseDTO.setDescription(jsonNode.has("description") ? jsonNode.get("description").asText() : "No description available");
            placeSummaryResponseDTO.setExtract(jsonNode.has("extract") ? jsonNode.get("extract").asText() : "No extract available");
            placeSummaryResponseDTO.setThumbnailUrl(jsonNode.has("thumbnail") && jsonNode.get("thumbnail").has("source")
                    ? jsonNode.get("thumbnail").get("source").asText() : null);

        } catch (Exception e) {
            log.error("Error fetching city summary from Wikipedia: ", e);
        }
        log.info("PlaceSummaryClient::fetchCitySummary END");
        return placeSummaryResponseDTO;
    }
}
