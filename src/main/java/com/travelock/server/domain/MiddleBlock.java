package com.travelock.server.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "category_code", nullable = false, unique = true, columnDefinition = "VARCHAR(10) COMMENT '카테고리 코드'")
    private String categoryCode;

    @Column(name = "category_name", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '카테고리 이름'")
    private String categoryName;

    // 미들 1 스몰 N
    @OneToMany(mappedBy = "middleBlock")
    @JsonManagedReference
    private List<SmallBlock> smallBlocks;


//    // 세터 대체 메서드 -> DB에 넣고 쓰는데 굳이 필요할까요.
//    public void setCategoryData(String categoryCode, String categoryName) {
//        this.categoryCode = categoryCode;
//        this.categoryName = categoryName;
//    }



}