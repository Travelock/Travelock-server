package com.travelock.server.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.travelock.server.dto.block.SearchResponseDTO;
import com.travelock.server.exception.base_exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmallBlockSearchClient {

    private final String KAKAO_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl(KAKAO_SEARCH_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build();

    public List<SearchResponseDTO> searchSmallBlockByKeyword(String keyword) {
        log.info("SmallBlockSearchClient::searchSmallBlockByKeyword START - keyword: {}", keyword);

        JsonNode jsonNode = webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("query", keyword).build())
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(status -> status.is5xxServerError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error))))
                .bodyToMono(JsonNode.class)
                .block(); // 동기적 호출

        JsonNode documents = jsonNode.get("documents");

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

        log.info("SmallBlockSearchClient::searchSmallBlockByKeyword END");
        return results;
    }
}
