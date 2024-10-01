package com.travelock.server.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter  // 아노테이션이 왜 작동하지 않는걸까..! 어쩔 수 없이 세터 사용
@AllArgsConstructor
@NoArgsConstructor
public class SmallBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smallBlockId;


    // Small Block : Middle Block = N : 1
    // 이 말은 즉, 여러개의 스몰블록이 하나의 미들블록을 참조할 수 있다는 것.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "middle_block_id")
    @JsonBackReference
    private MiddleBlock middleBlock;


    private String placeId;
    private String placeName;
    private String mapX;
    private String mapY;
    private String url;


    @Column(columnDefinition = "INT COMMENT '일정에 추가된(참조된) 수'")
    private Integer referenceCount;



    @OneToMany(mappedBy = "smallBlock", fetch = FetchType.LAZY)
    private List<SmallBlockReview> smallBlockReviews;

    public SmallBlock(MiddleBlock middleBlock, String placeId, String placeName, String mapX, String mapY, String url) {
        this.middleBlock = middleBlock;
        this.placeId = placeId;
        this.placeName = placeName;
        this.mapX = mapX;
        this.mapY = mapY;
        this.url = url;
        this.referenceCount = 0; // 레퍼카운트 초기값 0
    }

    // 레퍼 카운트 1씩 증가
    public void incrementReferenceCount() {
        this.referenceCount++;
    }
}