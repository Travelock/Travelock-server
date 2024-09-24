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
    // Full Block : Big Block = 1 : 1
    @OneToOne
    @JoinColumn(name = "big_block_id", nullable = false)
    private BigBlock bigBlock;

    // Full Block : Middle Block = 1 : 1
    @OneToOne
    @JoinColumn(name = "middle_block_id", nullable = false)
    private MiddleBlock middleBlock;

    // Full Block : Small Block = 1 : 1
    @OneToOne
    @JoinColumn(name = "small_block_id", nullable = false)
    private SmallBlock smallBlock;

    // Full Block : Daily Block Connect = 1 : N
    @OneToMany(mappedBy = "fullBlock")
    private List<DailyBlockConnect> dailyBlockConnects;
}

