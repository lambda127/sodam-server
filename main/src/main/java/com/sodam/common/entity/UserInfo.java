package com.sodam.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "is_active", insertable = false)
    private boolean isActive;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
}
