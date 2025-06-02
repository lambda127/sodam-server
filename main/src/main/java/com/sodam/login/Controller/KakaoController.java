package com.sodam.login.Controller;

import com.sodam.login.Service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("kakaoApiKey", kakaoService.getKakaoApiKey());
        model.addAttribute("redirectUri", kakaoService.getKakaoRedirectUri());
        return "login";
    }

    // 카카오 리다이렉트 콜백 핸들러 (API용)
    @GetMapping("/api/v1/auth/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code) {
        System.out.println("✅ 카카오 인가 코드 수신: " + code);
        return ResponseEntity.ok("인가 코드 수신 성공! code = " + code);
    }

    // 카카오 로그인 인증 후 사용자가 리디렉션되는 콜백 경로
    @RequestMapping("/api/v1/auth/kakao/callback")
    public String kakaoLogin(@RequestParam String code){
        // 카카오에서 전달받은 인증 코드(code)를 사용해 access token 요청
        String accessToken = kakaoService.GetAccessToken(code);
        // 발급받은 access token으로 사용자 정보 조회
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        String nickname = (String)userInfo.get("nickname");
        String email = (String)userInfo.get("email");

        System.out.println("nickname = " + nickname);
        System.out.println("email = " + email);
        System.out.println("accessToken = " + accessToken);

        return "redirect:/result";
    }

}