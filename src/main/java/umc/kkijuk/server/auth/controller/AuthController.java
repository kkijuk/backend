package umc.kkijuk.server.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.auth.service.AuthService;

import java.util.Map;

@Slf4j
@Tag(name = "social login", description = "소셜로그인 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/kakao/login")
    @Operation(summary = "카카오 로그인", description = "카카오 OAuth 인증을 통해 사용자 정보를 처리하고 JWT 토큰을 생성하여 반환합니다.",
    parameters = {
        @Parameter(name = "code", description = "카카오에서 발급된 인증 코드", required = true),
        @Parameter(name = "redirect_uri", description = "카카오에서 인가코드 받아오는 uri", required = true)
    })
    public ResponseEntity<Map<String, Object>> kakaoCallback(@RequestParam("code") String code,
                                                             @RequestParam("redirect_uri") String redirectUri) {

            Map<String, Object> tokens = authService.handleKakaoLogin(code, redirectUri);
            log.info("카카오 로그인 성공");
            return ResponseEntity.ok(tokens);

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
            Map<String, Object> tokens = authService.handleNaverLogin(code, state);
            log.info("네이버 로그인 성공");
            return ResponseEntity.ok(tokens);

        }catch (Exception e){
            log.error("네이버 인증 처리 중 오류 발생 : {}",e.getMessage(),e);
            return ResponseEntity.internalServerError().body(Map.of("error", "네이버 인증 처리 실패"));
        }
    }

}
