package com.travelock.server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BigBlock extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bigBlockId;  // 빅블럭 고유 ID

    @Column(columnDefinition = "VARCHAR(3) COMMENT '시/구 코드(3자리)'")
    private String cityCode;  // 시/구 코드 (3자리)

    @Column(columnDefinition = "VARCHAR(50) COMMENT '시/구 이름'")
    private String cityName;  // 시/구 이름

    // BigBlock이 하나의 State를 참조 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_info_id", nullable = false)
    private State state;  // State 테이블 참조

    // 미들블록 관계
    @OneToMany(mappedBy = "bigBlock")
    private List<MiddleBlock> middleBlocks = new ArrayList<>();  // 중간 블록 리스트
}