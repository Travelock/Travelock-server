package com.travelock.server.controller;

import com.travelock.server.dto.SmallBlockReviewDto;
import com.travelock.server.service.SmallBlockReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/block/small/review")
@RequiredArgsConstructor
public class SmallBlockReviewController {
    private final SmallBlockReviewService smallBlockReviewService;

    @GetMapping("/all/{smallBlockId}")
    public ResponseEntity<?> getAllReviews(@PathVariable Long smallBlockId){
        List<SmallBlockReviewDto> reviews = smallBlockReviewService.getAllReviews(smallBlockId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/my/{smallBlockId}")
    public ResponseEntity<?> getMyReviews(@PathVariable Long memberId){
        List<SmallBlockReviewDto> reviews = smallBlockReviewService.getMyReviews(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/{smallBlockReviewId}")
    public ResponseEntity<?> getReview(@PathVariable Long smallBlockReviewId){
        SmallBlockReviewDto reviews = smallBlockReviewService.getReview(smallBlockReviewId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }


    @PostMapping("/")
    public ResponseEntity<?> addReview(@RequestBody SmallBlockReviewDto smallBlockReviewDto){
        smallBlockReviewService.addReview(
                smallBlockReviewDto.getMemberId(),
                smallBlockReviewDto.getSmallBlockId(),
                smallBlockReviewDto.getReview());
        return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 작성 성공");
    }

    @PutMapping("/{smallBlockReviewId}")
    public ResponseEntity<?> modifyReview(@RequestBody SmallBlockReviewDto smallBlockReviewDto){
        smallBlockReviewService.modifyReview(
                smallBlockReviewDto.getSmallBlockReviewId(),
                smallBlockReviewDto.getReview());
        return ResponseEntity.status(HttpStatus.OK).body("리뷰 수정 성공");
    }

    @DeleteMapping("/{smallBlockReviewId}")
    public ResponseEntity<?> removeReview(@PathVariable Long smallBlockReviewId){
        smallBlockReviewService.removeReview(smallBlockReviewId);
        return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제 성공");
    }



}
