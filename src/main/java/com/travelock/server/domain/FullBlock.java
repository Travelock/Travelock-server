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
public class FullBlock extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fullBlockId;

    // B,M,S Block은 Full Block 사용시 항상 사용됨 - FetchType.EAGER(default)
    // FullBlock : BigBlock = N:1
    @ManyToOne
    @JoinColumn(name = "big_block_id", nullable = false)
    private BigBlock bigBlock;

    // FullBlock : MiddleBlock = N:1
    @ManyToOne
    @JoinColumn(name = "middle_block_id", nullable = false)
    private MiddleBlock middleBlock;

    // FullBlock : SmallBlock = N:1
    @ManyToOne
    @JoinColumn(name = "small_block_id", nullable = false)
    private SmallBlock smallBlock;

    // Full Block : Daily Block Connect = 1 : N
    @OneToMany(mappedBy = "fullBlock", fetch = FetchType.LAZY)
    private List<DailyBlockConnect> dailyBlockConnects;

    public void newFullBlock(BigBlock bigBlock, MiddleBlock middleBlock, SmallBlock smallBlock){
        this.bigBlock = bigBlock;
        this.middleBlock = middleBlock;
        this.smallBlock = smallBlock;
    }
}

