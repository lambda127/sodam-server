package com.sodam.review.dto;

import lombok.*;

import java.sql.Timestamp;

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
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
