package com.sodam.review.repository;

import com.sodam.common.entity.Place;
import com.sodam.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPlaceAndIdGreaterThanOrderByIdAsc(Place place, long cursor, Pageable of);
}
