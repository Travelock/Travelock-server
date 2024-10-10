package com.travelock.server.controller;

import com.travelock.server.dto.directions.DirectionsRequestDTO;
import com.travelock.server.service.DirectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/directions")
@Slf4j
@RequiredArgsConstructor
public class DirectionsController {
    private final DirectionsService directionsService;

    @PostMapping
    public ResponseEntity<?> searchDirections(@RequestBody List<DirectionsRequestDTO> requestDTO) {
        return ResponseEntity.ok(directionsService.searchDirections(requestDTO));
                //.thenApply(directionsResponseDTO -> ResponseEntity.ok(directionsResponseDTO))
                //.exceptionally(error -> {
                //    log.error("DirectionsController::searchDirections ERROR : " + error.getMessage());
                //    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                //});
    }
}
