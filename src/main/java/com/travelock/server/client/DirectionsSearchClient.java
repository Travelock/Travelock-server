package com.travelock.server.client;

import com.travelock.server.dto.DirectionsRequestDTO;
import com.travelock.server.dto.DirectionsResponseDTO;
import com.travelock.server.exception.base_exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DirectionsSearchClient {
    private final String TMAP_DIRECTIONS_URL = "https://apis.openapi.sk.com/transit/routes";

    @Value("${TMAP_API_KEY}")
    private String TMAP_API_KEY;
    public DirectionsResponseDTO requestSearchDirections(DirectionsRequestDTO requestDTO) {
        log.info("DirectionsSearchClient::requestSearchDirections START");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("appKey", TMAP_API_KEY);

        WebClient webClient = WebClient.builder()
                .baseUrl(TMAP_DIRECTIONS_URL)
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(headers))
                .build();


        // Request API
        // CompletableFuture<DirectionsResponseDTO> future = new CompletableFuture<>();
        DirectionsResponseDTO responseDTO = webClient.post()
                .bodyValue(requestDTO) // 요청 데이터
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse -> // 400대 에러 발생
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new BadRequestException(error))))
                .onStatus(status -> status.is5xxServerError(), clientResponse -> // 500대 에러 발생
                        clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error))))
                .bodyToMono(DirectionsResponseDTO.class)
                //.doOnSuccess(response -> {
                    // future.complete(response); // 성공 응답 데이터 세팅
                //})
                .block();

        log.info("DirectionsSearchClient::requestSearchDirections END");
         return responseDTO;
    }
}
