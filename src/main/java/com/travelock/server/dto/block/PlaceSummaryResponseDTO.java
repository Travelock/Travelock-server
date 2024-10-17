package com.travelock.server.dto.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceSummaryResponseDTO {
    private String title;
    private String description;
    private String extract;
    private String thumbnailUrl;  // 이미지가 있는 경우
}
