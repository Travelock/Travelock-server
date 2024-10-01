package com.travelock.server.dto.course.daily_create;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallBlockDto {
    private String mapX;
    private String mapY;
    private String placeId;
    private Integer referenceCount;
}



