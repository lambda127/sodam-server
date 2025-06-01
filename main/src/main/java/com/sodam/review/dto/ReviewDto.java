package com.sodam.review.dto;

import com.sodam.review.entity.Review;
import com.sodam.review.entity.ReviewPhoto;
import com.sodam.review.entity.ReviewTag;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private Long placeId;
    private Long userId;
    private String content;
    private List<ReviewPhotoDto> images;
    private List<ReviewTagDto> tags;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static ReviewDto fromEntity(Review review, List<ReviewTag> tags, List<ReviewPhoto> images) {
        return ReviewDto.builder()
                .id(review.getId())
                .placeId(review.getPlace().getId())
                .userId(review.getUserInfo().getId())
                .content(review.getContent())
                .tags(tags.stream().map(ReviewTagDto::fromEntity)
                        .collect(Collectors.toList()))
                .images(images.stream().map(ReviewPhotoDto::fromEntity).collect(Collectors.toList()))
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
