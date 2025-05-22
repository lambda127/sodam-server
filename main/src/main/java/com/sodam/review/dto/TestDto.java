package com.sodam.review.dto;

import com.sodam.review.entity.Place;
import com.sodam.review.entity.Review;
import com.sodam.review.entity.UserInfo;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestDto {
    private Place place;
    private UserInfo userInfo;
    private Review review;
}
