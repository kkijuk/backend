package umc.kkijuk.server.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.auth.jwt.JwtUtil;
import umc.kkijuk.server.auth.service.KakaoAuthService;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginWithKakao(@RequestHeader("Authorization") String kakaoAccessToken) {
        try {
            // Bearer 제거
            if (kakaoAccessToken.toLowerCase().startsWith("bearer ")) {
                kakaoAccessToken = kakaoAccessToken.substring(7).trim();
            }

            // 카카오 사용자 정보 가져오기
            Map<String, Object> kakaoUserInfo = kakaoAuthService.getKakaoUserInfo(kakaoAccessToken);
            log.info("카카오 사용자 정보: {}", kakaoUserInfo);

            // 사용자 정보 추출
            String email = kakaoAuthService.extractEmail(kakaoUserInfo);
            String name = kakaoAuthService.extractName(kakaoUserInfo);
            String phoneNumber = kakaoAuthService.extractPhoneNumber(kakaoUserInfo);
            LocalDate birthDate = kakaoAuthService.extractBirthDate(kakaoUserInfo);

//            // 이메일 검증
//            if (email == null || email.isEmpty()) {
//                log.error("카카오 사용자 정보에 이메일이 없습니다.");
//                return ResponseEntity.badRequest().body(Map.of("error", "카카오 사용자 정보에 이메일이 없습니다."));
//            }

            // 핸드폰 번호 검증
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                log.error("카카오 사용자 정보에 핸드폰 번호가 없습니다.");
                return ResponseEntity.badRequest().body(Map.of("error", "카카오 사용자 정보에 핸드폰 번호가 없습니다."));
            }

            Member member = memberService.findByPhoneNumber(phoneNumber);
            if (member == null) {
                log.info("신규 사용자 생성 - 핸드폰 번호: {}", phoneNumber);
                member = memberService.createMember(email, name, phoneNumber, birthDate);
            }

            // JWT 토큰 생성
            String accessToken = jwtUtil.createAccessToken(member.getEmail());
            String refreshToken = jwtUtil.createRefreshToken(member.getEmail());

            // 응답 데이터 생성
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);

            log.info("로그인 성공: 이메일={}, accessToken 생성 완료", email);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("카카오 로그인 처리 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "로그인 처리 실패"));
        }
    }
}
