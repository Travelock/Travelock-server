package com.travelock.server.dto;


import com.travelock.server.domain.SmallBlock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.MergedAnnotations;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDTO {

    private String placeId;
    private String placeName;
    private String  mapX;
    private String  mapY;
    private String categoryName;
    private String categoryCode;

}

