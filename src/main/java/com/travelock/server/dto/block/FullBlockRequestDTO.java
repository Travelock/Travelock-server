package com.travelock.server.dto.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullBlockRequestDTO {
    private Integer blockNum;
    private Long bigBlockId;
    private Long middleBlockId;
    private SmallBlockRequestDTO smallBlockDto;
}
