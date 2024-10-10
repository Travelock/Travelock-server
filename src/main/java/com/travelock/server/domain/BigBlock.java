package com.travelock.server.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    @JsonBackReference
    private State state;  // State 테이블 참조

}
