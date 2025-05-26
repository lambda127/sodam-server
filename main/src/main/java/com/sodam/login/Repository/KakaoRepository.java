package com.sodam.login.Repository;

import com.sodam.login.Domain.KakaoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoRepository extends JpaRepository<KakaoUser, String> {

    Optional<KakaoUser> findByid(Long id);
    Optional<KakaoUser> findByNickname(String nickname);
    Optional<KakaoUser> findByEmail(String email);

}