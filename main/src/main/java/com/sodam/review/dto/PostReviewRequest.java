package com.sodam.review.dto;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReviewRequest {
    private Long placeId;
    private Long userId;
    private String content;
    private List<String> tags;
}
