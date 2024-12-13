package umc.kkijuk.server.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import umc.kkijuk.server.auth.jwt.JwtUtil;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.repository.MemberRepository;
import umc.kkijuk.server.member.service.MemberService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthService {

    private final RestTemplate restTemplate;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    public String getKakaoAccessToken(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUri,
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    Map.class
            );

            log.info("카카오 액세스 토큰 응답: {}", response.getBody());
            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            log.error("카카오 액세스 토큰 요청 실패: {}", e.getMessage(), e);
            throw new RuntimeException("카카오 액세스 토큰 요청 실패", e);
        }
    }


    public Map<String, Object> getKakaoUserInfo(String accessToken) {
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, Map.class);
            log.info("카카오 사용자 정보 응답: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.error("카카오 사용자 정보 요청 실패: {}", e.getMessage());
            throw new RuntimeException("카카오 사용자 정보 요청 실패", e);
        }
    }

    public Member processKakaoUser(String accessToken) {
        Map<String, Object> kakaoUserInfo = getKakaoUserInfo(accessToken);
        Long kakaoId = Long.valueOf(kakaoUserInfo.get("id").toString());
        String email = extractEmail(kakaoUserInfo);
        String name = extractName(kakaoUserInfo);
        String phoneNumber = extractPhoneNumber(kakaoUserInfo);
        LocalDate birthDate = extractBirthDate(kakaoUserInfo);

        log.info("카카오 사용자 정보 추출 - 이메일: {}, 이름: {}, 카카오 ID: {}, 전화번호: {}, 생년월일: {}", email, name, kakaoId, phoneNumber, birthDate);

        return memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    log.info("신규 사용자 생성 - 카카오 ID: {}", kakaoId);
                    return memberService.createUserWithKakaoId(kakaoId, kakaoUserInfo);
                });
    }

    public Map<String, String> generateTokens(Member member) {
        String accessToken = jwtUtil.createAccessToken(member.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(member.getEmail());

        log.info("JWT 토큰 생성 완료 - 이메일: {}, accessToken: {}, refreshToken: {}", member.getEmail(), accessToken, refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }


    public String extractEmail(Map<String, Object> kakaoUserInfo) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUserInfo.get("kakao_account");
        return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
    }

    public String extractName(Map<String, Object> kakaoUserInfo) {
        Map<String, Object> profile = (Map<String, Object>) ((Map<String, Object>) kakaoUserInfo.get("kakao_account")).get("profile");
        return profile != null ? (String) profile.get("nickname") : null;
    }

    public String extractPhoneNumber(Map<String, Object> kakaoUserInfo) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUserInfo.get("kakao_account");
        return kakaoAccount != null ? (String) kakaoAccount.get("phone_number") : null;
    }

    public LocalDate extractBirthDate(Map<String, Object> kakaoUserInfo) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUserInfo.get("kakao_account");
        if (kakaoAccount == null) return null;

        String birthday = (String) kakaoAccount.get("birthday");
        String birthyear = (String) kakaoAccount.get("birthyear");

        if (birthday == null || birthday.isEmpty()) {
            return null;
        }

        int year = (birthyear != null && !birthyear.isEmpty())
                ? Integer.parseInt(birthyear)
                : LocalDate.now().getYear();

        int month = Integer.parseInt(birthday.substring(0, 2));
        int day = Integer.parseInt(birthday.substring(2, 4));

        return LocalDate.of(year, month, day);
    }
}
