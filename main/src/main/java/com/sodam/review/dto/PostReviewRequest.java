package com.sodam.review.dto;

import jakarta.validation.constraints.Size;
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

    @Size(min = 1, max = 100)
    private String content;

    private List<String> tags;
}
