package com.travelock.server.domain;

import com.travelock.server.domain.BigBlock;
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
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stateId;  // 고유 ID

    @Column(columnDefinition = "VARCHAR(2) COMMENT '도/광역시/특별시 코드(2자리)'")
    private String stateCode;  // 시/도 코드 (2자리)

    @Column(columnDefinition = "VARCHAR(50) COMMENT '도/광역시/특별시 이름'")
    private String stateName;  // 시/도 이름

    // State가 여러 BigBlock을 참조하는 관계 (1:N)
    @OneToMany(mappedBy = "state")
    private List<BigBlock> bigBlockList = new ArrayList<>();  // BigBlock 리스트 초기화
}