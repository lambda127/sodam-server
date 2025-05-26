package com.sodam.login.Service;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface KakaoService {

    String GetAccessToken(String code);
    HashMap<String, Object> getUserInfo(String accessToken);
    String getKakaoApiKey();
    String getKakaoRedirectUri();
    void kakaoLogout(String accessToken);
}
