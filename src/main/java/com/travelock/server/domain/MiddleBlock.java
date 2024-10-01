package com.travelock.server.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor // 이거 왜 작동 안되는지... 아노테이션...
@AllArgsConstructor
public class MiddleBlock extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long middleBlockId;

    @Column(name = "category_code", nullable = false, unique = true)
    private String categoryCode;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    // 결국 생성자 추가,,,
    public MiddleBlock(String categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }

    //
//    // Middle Block : Big Block = N : 1
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "big_block_id")
//    private BigBlock bigBlock;

    // Middle Block : Small Block = 1 : N
    @OneToMany(mappedBy = "middleBlock")
    @JsonManagedReference
    private List<SmallBlock> smallBlocks;
}