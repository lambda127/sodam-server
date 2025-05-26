package com.sodam.login.Controller;

import com.sodam.login.Service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // 카카오 로그인 인증 후 사용자가 리디렉션되는 콜백 경로
    @RequestMapping("/login/oauth2/code/kakao")
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