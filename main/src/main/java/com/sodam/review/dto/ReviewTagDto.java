package com.sodam.review.dto;

import com.sodam.review.entity.ReviewTag;
import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewTagDto {
    private Long id;
    private Long review_id;
    private String content;

    public static ReviewTagDto fromEntity(ReviewTag reviewTag) {
        return ReviewTagDto.builder()
                .id(reviewTag.getId())
                .review_id(reviewTag.getReview().getId())
                .content(reviewTag.getContent())
                .build();
    }
}
