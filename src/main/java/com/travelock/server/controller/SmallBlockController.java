package com.travelock.server.controller;

import com.travelock.server.domain.SmallBlock;
import com.travelock.server.dto.SearchResponseDTO;
import com.travelock.server.dto.SmallBlockRequestDTO;
import com.travelock.server.service.SmallBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/api/small")
@RequiredArgsConstructor
public class SmallBlockController {

    private final SmallBlockService smallBlockService;

    // 키워드로 장소 검색 (DB에 저장되지 않음)
    @GetMapping("/search")
    public ResponseEntity<List<SearchResponseDTO>> searchSmallBlock(@RequestParam String keyword) {
        try {
            List<SearchResponseDTO> results = smallBlockService.searchSmallBlockByKeyword(keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error searching SmallBlock", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 사용자가 선택한 장소를 DB에 저장 (코스 확정 시 호출)
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmSmallBlock(@RequestBody SmallBlockRequestDTO requestDTO) {
        try {
            SmallBlock smallBlock = smallBlockService.confirmAndCreateSmallBlock(requestDTO);
            return new ResponseEntity<>("SmallBlock saved successfully", HttpStatus.CREATED); // 201 !!
        } catch (Exception e) {
            log.error("Error confirming SmallBlock", e);  // 에러 메시지는 로그에 기록
            return new ResponseEntity<>("Error saving SmallBlock", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 전체 스몰블록 조회
    @GetMapping("/list")
    public ResponseEntity<List<SmallBlock>> getAllSmallBlocks() {
        List<SmallBlock> smallBlocks = smallBlockService.getAllSmallBlocks();
        return ResponseEntity.ok(smallBlocks);
    }

    // 특정 스몰블록 조회 (id로)
    @GetMapping("/{id}")
    public ResponseEntity<SmallBlock> getSmallBlockById(@PathVariable Long id) {
        try {
            SmallBlock smallBlock = smallBlockService.getSmallBlockById(id);
            return ResponseEntity.ok(smallBlock);
        } catch (Exception e) {
            log.error("Error fetching SmallBlock by id", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
