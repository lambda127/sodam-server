package com.sodam.review.service;

import com.sodam.common.entity.Place;
import com.sodam.common.entity.UserInfo;
import com.sodam.common.repository.PlaceRepository;
import com.sodam.common.repository.UserInfoRepository;
import com.sodam.common.service.FileStorageService;
import com.sodam.review.dto.PostReviewRequest;
import com.sodam.review.dto.ReviewDto;
import com.sodam.review.entity.Review;
import com.sodam.review.entity.ReviewPhoto;
import com.sodam.review.entity.ReviewTag;
import com.sodam.review.repository.ReviewPhotoRepository;
import com.sodam.review.repository.ReviewRepository;
import com.sodam.review.repository.ReviewTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewTagRepository reviewTagRepository;
    private final PlaceRepository placeRepository;
    private final UserInfoRepository userInfoRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public ReviewDto postReview(PostReviewRequest request, List<MultipartFile> images) {
        UserInfo userInfo = userInfoRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Place place = placeRepository.findById(request.getPlaceId())
                .orElseThrow(() -> new RuntimeException("Place not found"));
        Review review = Review.builder()
                .userInfo(userInfo)
                .place(place)
                .content(request.getContent())
                .build();
        Review savedReview = reviewRepository.save(review);

        List<ReviewTag> reviewTags = new ArrayList<>();
        for (String tag : request.getTags()) {
            ReviewTag reviewTag = ReviewTag.builder()
                    .review(savedReview)
                    .content(tag)
                    .build();
            reviewTags.add(reviewTag);
        }

        reviewTagRepository.saveAll(reviewTags); //태그 저장

        List<ReviewPhoto> reviewPhotos = new ArrayList<>();
        if(images != null){
            for (MultipartFile image : images) {
                try {
                    String url = fileStorageService.store(image);
                    ReviewPhoto photo = ReviewPhoto.builder()
                            .review(review)
                            .photoUrl(url)
                            .build();
                    reviewPhotos.add(photo);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to store image", e);
                }
            }
            reviewPhotoRepository.saveAll(reviewPhotos); //이미지 메타데이터
        }

        return ReviewDto.fromEntity(savedReview, reviewTags, reviewPhotos);
    }

    @Transactional
    public ReviewDto removeReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        List<ReviewTag> reviewTag = reviewTagRepository.findByReview(review);
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.findByReview(review);
        reviewTagRepository.deleteAll(reviewTag);
        reviewRepository.delete(review);
        reviewPhotoRepository.deleteAll(reviewPhotos);

        return ReviewDto.fromEntity(review, reviewTag, reviewPhotos);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviews(Long placeId, Long lastId, int size) {
        long cursor = lastId == null ? 0L : lastId;
        int pageSize = (size <= 0) ? 10 : size;

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("Place not found"));

        List<Review> reviews = reviewRepository.findByPlaceAndIdGreaterThanOrderByIdAsc(
                place, cursor, PageRequest.of(0, pageSize));

        List<ReviewDto> reviewDtos = new ArrayList<>();
        for(Review review : reviews){
            List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.findByReview(review);
            List<ReviewTag> reviewTags = reviewTagRepository.findByReview(review);
            reviewDtos.add(ReviewDto.fromEntity(review, reviewTags, reviewPhotos));
        }

        return reviewDtos;
    }
}
