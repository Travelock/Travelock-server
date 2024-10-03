package com.travelock.server.dto;

import com.travelock.server.domain.SmallBlock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallBlockResponseDTO {
    private Long smallBlockId;    // SmallBlock 고유 ID
    private String placeId;       // 장소 ID
    private String placeName;     // 장소 이름
    private String mapX;          // 지도 X 좌표
    private String mapY;          // 지도 Y 좌표
    private Integer referenceCount; // 참조 횟수

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
                smallBlock.getReferenceCount()
        );
    }
}
