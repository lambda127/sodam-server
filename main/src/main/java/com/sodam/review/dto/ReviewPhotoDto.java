package com.sodam.review.dto;

import com.sodam.review.entity.ReviewPhoto;
import lombok.*;

import java.sql.Timestamp;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewPhotoDto {
    private Long id;
    private Long review_id;
    private String photoUrl;
    private Timestamp createdAt;

    public static ReviewPhotoDto fromEntity(ReviewPhoto reviewPhoto) {
        return ReviewPhotoDto.builder()
                .id(reviewPhoto.getId())
                .review_id(reviewPhoto.getReview().getId())
                .photoUrl(reviewPhoto.getPhotoUrl())
                .createdAt(reviewPhoto.getCreatedAt())
                .build();
    }
}
