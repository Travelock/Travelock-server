package com.travelock.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SmallBlockReview extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smallBlockReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "small_block_id")
    private SmallBlock smallBlock;

    @Column(columnDefinition = "varchar(255) COMMENT '명소/핫플레이스 단위 이름'")
    private String review;
    @Size(max = 1) @Column(columnDefinition = "varchar(1) COMMENT '활성화 상태'")
    private String activeStatus;


    public void addReview(Member member, SmallBlock smallBlock, String review){
        this.member = member;
        this.smallBlock = smallBlock;
        this.review = review;
    }




}
