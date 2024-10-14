package com.travelock.server.dto.block;

import com.travelock.server.domain.BigBlock;
import com.travelock.server.domain.SmallBlock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmallBlockResponseDTO {
    private Long smallBlockId;
    private String placeId;
    private String placeName;
    private String mapX;
    private String mapY;
    private Integer referenceCount;
    private String categoryName;
    private Long bigBlockId;

    public static SmallBlockResponseDTO fromDomainToResponseDTO(SmallBlock smallBlock) {
        if (smallBlock == null) {
            return null;  // SmallBlock이 null일 경우 null 반환
        }

        return new SmallBlockResponseDTO(
                smallBlock.getSmallBlockId(),
                smallBlock.getPlaceId(),
                smallBlock.getPlaceName(),
                smallBlock.getMapX(),
                smallBlock.getMapY(),
                smallBlock.getReferenceCount(),
                smallBlock.getMiddleBlock().getCategoryName(),
                smallBlock.getBigBlock().getBigBlockId()
        );
    }

}
