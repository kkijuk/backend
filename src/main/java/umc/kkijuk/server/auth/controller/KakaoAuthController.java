package umc.kkijuk.server.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.auth.service.KakaoAuthService;
import umc.kkijuk.server.member.domain.Member;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/auth/kakao/login")
    public ResponseEntity<Map<String, Object>> kakaoCallback(@RequestParam("code") String code) {
        try {
            // 1. 카카오 액세스 토큰 발급
            String kakaoAccessToken = kakaoAuthService.getKakaoAccessToken(code);

            // 2. 카카오 사용자 정보 처리 및 사용자 생성/조회
            Member member = kakaoAuthService.processKakaoUser(kakaoAccessToken);

            // 3. JWT 토큰 생성
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("Token", kakaoAuthService.generateTokens(member));

            log.info("카카오 로그인 성공: 사용자 이름={}, 카카오 ID={}", member.getName(), member.getSocialId());
            return ResponseEntity.ok(tokens);

        } catch (Exception e) {
            log.error("카카오 인증 처리 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "카카오 인증 처리 실패"));
        }
    }
}
