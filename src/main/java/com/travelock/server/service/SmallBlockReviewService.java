package com.travelock.server.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelock.server.domain.QMember;
import com.travelock.server.domain.QSmallBlock;
import com.travelock.server.domain.QSmallBlockReview;
import com.travelock.server.domain.SmallBlockReview;
import com.travelock.server.dto.SmallBlockReviewDto;
import com.travelock.server.exception.review.AddReviewException;
import com.travelock.server.exception.ResourceNotFoundException;
import com.travelock.server.exception.review.ReviewModificationException;
import com.travelock.server.repository.SmallBlockReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmallBlockReviewService {
    private final JPAQueryFactory query;
    private final SmallBlockReviewRepository smallBlockReviewRepository;

    public List<SmallBlockReviewDto> getAllReviews(Long smallBlockId){
        QSmallBlockReview qSmallBlockReview = QSmallBlockReview.smallBlockReview;

        List<SmallBlockReviewDto> reviews = query.select(Projections.constructor(SmallBlockReviewDto.class,
                        qSmallBlockReview.smallBlockReviewId,
                        qSmallBlockReview.member.memberId,
                        qSmallBlockReview.member.nickName,
                        qSmallBlockReview.smallBlock.smallBlockId,
                        qSmallBlockReview.review
                )).from(qSmallBlockReview)
                .where(qSmallBlockReview.smallBlock.smallBlockId.eq(smallBlockId)
                        .and(qSmallBlockReview.activeStatus.eq("y")))
                .fetch();

        if (reviews == null) {
            throw new ResourceNotFoundException("Review not found with SmallBlock id: " + smallBlockId);
        }

        return reviews;

    }
    public List<SmallBlockReviewDto> getMyReviews(Long memberId){
        QSmallBlockReview qSmallBlockReview = QSmallBlockReview.smallBlockReview;

        List<SmallBlockReviewDto> reviews = query.select(Projections.constructor(SmallBlockReviewDto.class,
                        qSmallBlockReview.smallBlockReviewId,
                        qSmallBlockReview.member.memberId,
                        qSmallBlockReview.member.nickName,
                        qSmallBlockReview.smallBlock.smallBlockId,
                        qSmallBlockReview.review
                )).from(qSmallBlockReview)
                .where(qSmallBlockReview.member.memberId.eq(memberId)
                        .and(qSmallBlockReview.activeStatus.eq("y")))
                .fetch();

        if (reviews == null) {
            throw new ResourceNotFoundException("Review not found with Member id: " + memberId);
        }

        return reviews;
    }
    public SmallBlockReviewDto getReview(Long smallBlockReviewId){
        QSmallBlockReview qSmallBlockReview = QSmallBlockReview.smallBlockReview;

        SmallBlockReviewDto review = query.select(Projections.constructor(SmallBlockReviewDto.class,
                        qSmallBlockReview.smallBlockReviewId,
                        qSmallBlockReview.member.memberId,
                        qSmallBlockReview.member.nickName,
                        qSmallBlockReview.smallBlock.smallBlockId,
                        qSmallBlockReview.review
                )).from(qSmallBlockReview)
                .where(qSmallBlockReview.smallBlockReviewId.eq(smallBlockReviewId)
                        .and(qSmallBlockReview.activeStatus.eq("y")))
                .fetchOne();

        if (review == null) {
            throw new ResourceNotFoundException("Review not found with id: " + smallBlockReviewId);
        }

        return review;
    }
    public void addReview(Long memberId, Long smallBlockId, String review){
        QMember qMember = QMember.member;
        QSmallBlock qSmallBlock = QSmallBlock.smallBlock;

        Tuple tuple = query.select(qMember, qSmallBlock)
                .from(qMember)
                .join(qSmallBlock).on(qSmallBlock.smallBlockId.eq(smallBlockId))
                .where(qMember.memberId.eq(memberId))
                .fetchOne();

        if (tuple == null || tuple.get(qMember) == null || tuple.get(qSmallBlock) == null) {
            throw new AddReviewException("Member or SmallBlock not found");
        }

        SmallBlockReview smallBlockReview = new SmallBlockReview();

        smallBlockReview.addReview(
                tuple.get(qMember),
                tuple.get(qSmallBlock),
                review
        );

        try {
            smallBlockReviewRepository.save(smallBlockReview);
        } catch (Exception e) {
            log.error("Failed to add review. ", e);
            throw new AddReviewException("Failed to save review");
        }
    }
    public void modifyReview(Long smallBlockReviewId, String review){
        QSmallBlockReview qSmallBlockReview = QSmallBlockReview.smallBlockReview;

        long executed = query.update(qSmallBlockReview).set(qSmallBlockReview.review, review)
                .where(qSmallBlockReview.smallBlockReviewId.eq(smallBlockReviewId))
                .execute();

        if (executed != 1L) {
            throw new ReviewModificationException("Failed to modify review with id: " + smallBlockReviewId);
        }

    }
    public void removeReview(Long smallBlockReviewId, Long memberId){
        QSmallBlockReview qSmallBlockReview = QSmallBlockReview.smallBlockReview;

        long executed = query.update(qSmallBlockReview).set(qSmallBlockReview.activeStatus, "n")
                .where(qSmallBlockReview.smallBlockReviewId.eq(smallBlockReviewId)
                        .and(qSmallBlockReview.member.memberId.eq(memberId)))
                .execute();

        if (executed != 1L) {
            throw new ReviewModificationException("Failed to remove review with id: " + smallBlockReviewId);
        }

    }
}
