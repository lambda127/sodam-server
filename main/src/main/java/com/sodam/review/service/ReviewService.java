package com.sodam.review.service;

import com.sodam.common.entity.Place;
import com.sodam.common.entity.UserInfo;
import com.sodam.common.repository.PlaceRepository;
import com.sodam.common.repository.UserInfoRepository;
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
        List<ReviewTag> reviewTags = new ArrayList<>();
        for (String tag : request.getTags()) {
            ReviewTag reviewTag = ReviewTag.builder()
                    .review(review)
                    .content(tag)
                    .build();
            reviewTags.add(reviewTag);
        }

        List<ReviewPhoto> fileUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                String url = fileStorageService.store(image);
                ReviewPhoto photo = ReviewPhoto.builder()
                        .review(review)
                        .photoUrl(url)
                        .build();
                fileUrls.add(photo);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image", e);
            }
        }

        reviewTagRepository.saveAll(reviewTags); //태그 저장
        reviewPhotoRepository.saveAll(fileUrls); //이미지 메타데이터
        Review savedReview = reviewRepository.save(review);

        return ReviewDto.fromEntity(savedReview, reviewTags);
    }

    @Transactional
    public ReviewDto removeReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        List<ReviewTag> reviewTag = reviewTagRepository.findByReview(review);
        ReviewPhoto reviewPhoto = reviewPhotoRepository.findByReview(review)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        reviewTagRepository.deleteAll(reviewTag);
        reviewRepository.delete(review);
        reviewPhotoRepository.delete(reviewPhoto);

        return ReviewDto.fromEntity(review, reviewTag);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviews(String placeId, String lastId, int size) {
        long cursor = (lastId == null || lastId.isEmpty()) ? 0L : Long.parseLong(lastId);
        int pageSize = (size <= 0) ? 10 : size;

        List<Review> reviews = reviewRepository.findByPlaceIdAndIdGreaterThanOrderByIdAsc(
                Long.parseLong(placeId), cursor, PageRequest.of(0, pageSize));

        for(Review review : reviews) {

        }
    }
}
