package com.sodam.review.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "review_tags")
public class ReviewTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "review_id")
    @ManyToOne(fetch = FetchType.LAZY) //Review 정보는 개별적으로 참조
    private Review review;

    @Column(name = "content")
    private String content;
}
