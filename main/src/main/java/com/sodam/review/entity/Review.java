package com.sodam.review.entity;

import com.sodam.common.entity.Place;
import com.sodam.common.entity.UserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "place_id")
    @ManyToOne(fetch = FetchType.LAZY) //Place 정보는 개별적으로 참조
    private Place place;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserInfo userInfo;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false)
    private Timestamp updatedAt;
}
