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
    private String apiId;

    @Column(columnDefinition = "INT COMMENT '일정에 추가된(참조된) 수'")
    private Integer referenceCount;

    // Small Block : Middle Block = N : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "middle_block_id")
    private MiddleBlock middleBlock;

    @OneToMany(mappedBy = "smallBlock", fetch = FetchType.LAZY)
    private List<SmallBlockReview> smallBlockReviews;
}
