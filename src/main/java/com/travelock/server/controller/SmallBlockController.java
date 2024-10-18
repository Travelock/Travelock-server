package com.travelock.server.controller;

import com.travelock.server.converter.DTOConverter;
import com.travelock.server.domain.SmallBlock;
import com.travelock.server.dto.block.SearchResponseDTO;
import com.travelock.server.dto.block.SmallBlockRequestDTO;
import com.travelock.server.dto.block.SmallBlockResponseDTO;
import com.travelock.server.service.SmallBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/api/small")
@RequiredArgsConstructor
public class SmallBlockController {

    private final SmallBlockService smallBlockService;

    @Operation(summary = "키워드로 장소 검색",
            tags = {"스몰블록 API - V1"},
            description = "키워드로 장소를 검색.",
            parameters = {
                    @Parameter(name = "keyword", description = "검색 키워드", required = true, in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            })
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

    @Operation(summary = "시군구별 추천 스몰블록 조회",
            tags = {"스몰블록 API - V1"},
            description = "특정 시군구(BigBlock ID)에서 referenceCount가 높은 순으로 스몰블록을 조회.",
            parameters = {
                    @Parameter(name = "bigBlockId", description = "BigBlock ID (시군구 ID)", required = true, in = ParameterIn.QUERY),
                    @Parameter(name = "limit", description = "조회할 개수", required = true, in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(
                            schema = @Schema(implementation = SmallBlockResponseDTO.class),
                            mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            })

    @GetMapping("/popular/t1")
    public ResponseEntity<Map<Long, SmallBlockResponseDTO>> getPopularSmallBlocks() {


        /*각 도시별 추천 스몰블럭 하나씩만 가져오게 수정*/
        try {
            List<SmallBlock> popularSmallBlocks = smallBlockService.getPopularSmallBlocks();

            List<SmallBlockResponseDTO> dtos = DTOConverter.toDtoList(popularSmallBlocks, popular -> new SmallBlockResponseDTO(
                    popular.getSmallBlockId(),
                    popular.getPlaceId(),
                    popular.getPlaceName(),
                    popular.getMapX(),
                    popular.getMapY(),
                    popular.getReferenceCount(),
                    popular.getMiddleBlock().getCategoryName(),
                    popular.getBigBlock().getBigBlockId()
            ));

            Map<Long, SmallBlockResponseDTO> response = new HashMap<>();

            for (SmallBlockResponseDTO dto : dtos) {
                response.put(dto.getBigBlockId(), dto); // key: bigBlockId, value: dto
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching popular SmallBlocks by region", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "저장된 전체 스몰블록 조회",
            tags = {"스몰블록 API - V1"},
            description = "저장된 전체 스몰블록을 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            })
    @GetMapping("/list")
    public ResponseEntity<List<SmallBlockResponseDTO>> getAllSmallBlocks() {
        List<SmallBlockResponseDTO> smallBlocks = smallBlockService.getAllSmallBlocks();
        return ResponseEntity.ok(smallBlocks);
    }

    @Operation(summary = "저장된 특정 스몰블록 조회",
            tags = {"스몰블록 API - V1"},
            description = "저장된 특정 스몰블록을 id로 조회.",
            parameters = {
                    @Parameter(name = "id", description = "스몰블록 ID", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "스몰블록을 찾을 수 없음", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json"))
            })
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
