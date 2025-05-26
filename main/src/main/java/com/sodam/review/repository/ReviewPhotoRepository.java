package com.sodam.review.repository;

import com.sodam.review.entity.Review;
import com.sodam.review.entity.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {
    Optional<ReviewPhoto> findByReview(Review review);
}
