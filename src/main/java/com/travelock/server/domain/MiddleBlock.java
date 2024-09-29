package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MiddleBlock extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long middleBlockId;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '명소/특정지역 단위 이름'")
    private String categoryName;
    @Column(columnDefinition = "VARCHAR(255) COMMENT '명소/특정지역 단위 코드'")
    private String categoryCode;

    @Column(columnDefinition = "INT COMMENT '일정에 추가된(참조된) 수'")
    private Integer referenceCount;

    // Middle Block : Big Block = N : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "big_block_id")
    private BigBlock bigBlock;

    // Middle Block : Small Block = 1 : N
    @OneToMany(mappedBy = "middleBlock")
    private List<SmallBlock> smallBlocks;
}