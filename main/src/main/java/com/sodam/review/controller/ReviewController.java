package com.sodam.review.controller;

import com.sodam.review.dto.PostReviewRequest;
import com.sodam.review.dto.ReviewDto;
import com.sodam.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/post")
    public ResponseEntity<ReviewDto> postReview(
            @RequestPart PostReviewRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                reviewService.postReview(request, images));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> removeReview(
            @PathVariable Long reviewId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                reviewService.removeReview(reviewId));
    }

    @GetMapping("")
    public ResponseEntity<List<ReviewDto>> getReviews(
            @RequestParam Long place_id,
            @RequestParam(required = false) Long last_id,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                reviewService.getReviews(place_id, last_id, size));
    }
}
