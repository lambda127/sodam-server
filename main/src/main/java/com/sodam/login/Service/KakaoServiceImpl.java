package com.sodam.login.Service;

import com.sodam.login.API.KakaoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Primary
public class KakaoServiceImpl implements KakaoService {

    private final KakaoApi kakaoApi;

    @Override
    public String GetAccessToken(String code) {
        return kakaoApi.GetAccessToken(code);
    }

    @Override
    public HashMap<String, Object> getUserInfo(String accessToken) {
        return kakaoApi.getUserInfo(accessToken);
    }

    @Override
    public void kakaoLogout(String accessToken) {
        kakaoApi.kakaoLogout(accessToken);
    }

    @Override
    public String getKakaoApiKey() {
        return kakaoApi.getKakaoApiKey();
    }

    @Override
    public String getKakaoRedirectUri() {
        return kakaoApi.getKakaoRedirectUri();
    }
}

