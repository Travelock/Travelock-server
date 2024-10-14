package com.travelock.server.dto.block;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

