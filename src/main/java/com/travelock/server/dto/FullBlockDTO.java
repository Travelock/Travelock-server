package com.travelock.server.dto;

import com.travelock.server.domain.FullBlock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullBlockDTO {
    private Long fullBlockId;
    private Integer blockNum;
    private String stateCode;  // 시/도 코드 (2자리)
    private String stateName;  // 시/도 이름
    private String categoryCode;
    private String categoryName;
    private SmallBlockResponseDTO smallBlock;

    public static FullBlockDTO fromDomainToResponseDTO(FullBlock fullBlock, Integer blockNum) {
        // SmallBlock을 SmallBlockResponseDTO로 변환
        SmallBlockResponseDTO smallBlockDTO = SmallBlockResponseDTO.fromDomainToResponseDTO(fullBlock.getSmallBlock());

        return new FullBlockDTO(
                fullBlock.getFullBlockId(),
                blockNum,
                fullBlock.getBigBlock().getState().getStateCode(),
                fullBlock.getBigBlock().getState().getStateName(),
                fullBlock.getMiddleBlock().getCategoryCode(),
                fullBlock.getMiddleBlock().getCategoryName(),
                smallBlockDTO
        );
    }
}
