package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SmallBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smallBlockId;

    @Column(columnDefinition = "VARCHAR(50) COMMENT 'API에서 받은 고유값'")
    private String placeId;

    @Column(columnDefinition = "VARCHAR(100) COMMENT '경도값'")
    private String mapX;
    @Column(columnDefinition = "VARCHAR(100) COMMENT '위도값'")
    private String mapY;
    @Column(columnDefinition = "VARCHAR(255) COMMENT '카카오 Place 링크 주소'")
    private String linkURL;

    @Column(columnDefinition = "INT COMMENT '일정에 추가된(참조된) 수'")
    private Integer referenceCount;

    // Small Block : Middle Block = N : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "middle_block_id")
    private MiddleBlock middleBlock;

    @OneToMany(mappedBy = "smallBlock", fetch = FetchType.LAZY)
    private List<SmallBlockReview> smallBlockReviews;

    public void createNewSmallBlock(String mapX, String mapY, String linkURL, MiddleBlock middleBlock){
        this.mapX = mapX;
        this.mapY = mapY;
        this.linkURL = linkURL;
        this.middleBlock = middleBlock;
    }
}
