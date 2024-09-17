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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "small_block_id")
    private SmallBlock smallBlock;

    @Column(columnDefinition = "varchar(255)")
    private String review;
    @Size(max = 1) @Column(columnDefinition = "varchar(1)")
    private String activeStatus;


    public void addReview(Member member, SmallBlock smallBlock, String review){
        this.member = member;
        this.smallBlock = smallBlock;
        this.review = review;
    }




}
