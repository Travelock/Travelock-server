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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_info_id", nullable = false)
    @JsonBackReference
    private State state;  // State 테이블 참조

//    // 추후에 새로운 State가 생길 경우를 대비한 메서드, (빅블럭은 state에 소속되어야 하므로 bigblock 도메인에 위치)
//    public void setState(State state) {
//        this.state = state;
//        if (!state.getBigBlockList().contains(this)) {
//            state.addBigBlock(this);
//        }
//    }

}
