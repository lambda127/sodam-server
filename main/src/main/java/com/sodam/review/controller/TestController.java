package com.sodam.review.controller;

import com.sodam.review.dto.TestDto;
import com.sodam.review.repository.PlaceRepository;
import com.sodam.review.repository.ReviewRepository;
import com.sodam.review.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review/")
@RequiredArgsConstructor
public class TestController {
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final UserInfoRepository userInfoRepository;

    @Transactional
    @GetMapping("/test")
    public TestDto test() {
        return TestDto.builder()
                .place(placeRepository.findAll().get(0))
                .userInfo(userInfoRepository.findAll().get(0))
                .review(reviewRepository.findAll().get(0))
                .build();
    }
}
