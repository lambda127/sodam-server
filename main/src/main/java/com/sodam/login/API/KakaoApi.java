package com.sodam.login.API;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sodam.login.Repository.KakaoRepository;
import com.sodam.login.Service.KakaoService;
import com.sodam.login.Domain.KakaoUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@RequiredArgsConstructor
@Getter
@Component
public class KakaoApi implements KakaoService {

    private final KakaoRepository kakaoRepository;

    @Value("${kakao.api_key}")
    private String kakaoApiKey;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    //인가 코드 받아서 accessToken을 반환
    public String GetAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(kakaoApiKey);
            sb.append("&redirect_uri=").append(kakaoRedirectUri);
            sb.append("&code=").append(code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();

            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }
            String result = responseSb.toString();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        return accessToken;
    }

    //accessToken을 받아서 userInfo 반환
    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            int responseCode = conn.getResponseCode();

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }
            String result = responseSb.toString();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = null;
            JsonObject kakaoAccount = null;
            if (element.getAsJsonObject().get("properties") != null && element.getAsJsonObject().get("properties").isJsonObject()) {
                properties = element.getAsJsonObject().getAsJsonObject("properties");
            }
            if (element.getAsJsonObject().get("kakao_account") != null && element.getAsJsonObject().get("kakao_account").isJsonObject()) {
                kakaoAccount = element.getAsJsonObject().getAsJsonObject("kakao_account");
            }

            String nickname = null;

            if (kakaoAccount != null
                && kakaoAccount.has("profile")
                && kakaoAccount.getAsJsonObject("profile").has("nickname")) {
                nickname = kakaoAccount.getAsJsonObject("profile").get("nickname").getAsString();
            }

            if ((nickname == null || nickname.isEmpty())
                && properties != null
                && properties.has("nickname")) {
                nickname = properties.get("nickname").getAsString();
            }

            userInfo.put("nickname", nickname);

            String email = null;
            if (kakaoAccount != null && kakaoAccount.has("email")) {
                email = kakaoAccount.get("email").getAsString();
            }
            userInfo.put("email", email);

            if (nickname != null && email != null) {
                KakaoUser kakaouser = new KakaoUser(nickname, email);
                kakaoRepository.save(kakaouser);
            }


            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    //accessToken을 받아서 로그아웃 시키는 메서드
    public void kakaoLogout(String accessToken){
        String reqUrl = "https://kapi.kakao.com/v1/user/logout";

        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
