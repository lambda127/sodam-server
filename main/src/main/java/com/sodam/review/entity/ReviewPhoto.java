package com.sodam.review.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "review_photos")
public class ReviewPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "review_id")
    @ManyToOne(fetch = FetchType.LAZY) //Review 정보는 개별적으로 참조
    private Review review;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
}
