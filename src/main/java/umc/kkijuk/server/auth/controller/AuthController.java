package umc.kkijuk.server.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.auth.service.AuthService;
import umc.kkijuk.server.member.domain.Member;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "Auth", description = "소셜로그인 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/kakao/login")
    @Operation(summary = "카카오 로그인", description = "카카오 OAuth 인증을 통해 사용자 정보를 처리하고 JWT 토큰을 생성하여 반환합니다.")
    @Parameter(name = "code", description = "카카오에서 발급된 인증 코드", required = true)
    public ResponseEntity<Map<String, Object>> kakaoCallback(@RequestParam("code") String code) {
        try {
            // 1. 카카오 액세스 토큰 발급
            String kakaoAccessToken = authService.getKakaoAccessToken(code);

            // 2. 카카오 사용자 정보 처리 및 사용자 생성/조회
            Member member = authService.processKakaoUser(kakaoAccessToken);

            // 3. JWT 토큰 생성
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("Token", authService.generateTokens(member));

            log.info("카카오 로그인 성공: 사용자 이름={}, 카카오 ID={}", member.getName(), member.getSocialId());
            return ResponseEntity.ok(tokens);

        } catch (Exception e) {
            log.error("카카오 인증 처리 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "카카오 인증 처리 실패"));
        }
    }
    @GetMapping("/naver/login")
    @Operation(summary = "네이버 로그인", description = "네이버 OAuth 인증을 통해 사용자 정보를 처리하고 JWT 토큰을 생성하여 반환합니다.",
            parameters = {
                    @Parameter(name = "code", description = "네이버에서 발급된 인증 코드", required = true),
                    @Parameter(name = "state", description = "요청 검증을 위한 상태 값", required = true)
    })
    public ResponseEntity<Map<String,Object>> naverCallback(@RequestParam("code") String code,
                                                            @RequestParam("state") String state){
        try{
            String naverAccessToken = authService.getNaverAccessToken(code, state);
            Member member = authService.processNaverUser(naverAccessToken);
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("Token", authService.generateTokens(member));
            log.info("네이버 로그인 성공: 사용자 이름={}, 네이버 ID={}", member.getName(), member.getSocialId());
            return ResponseEntity.ok(tokens);

        }catch (Exception e){
            log.error("네이버 인증 처리 중 오류 발생 : {}",e.getMessage(),e);
            return ResponseEntity.internalServerError().body(Map.of("error", "네이버 인증 처리 실패"));
        }
    }

}
