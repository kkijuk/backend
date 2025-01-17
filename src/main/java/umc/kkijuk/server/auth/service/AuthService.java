package umc.kkijuk.server.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import umc.kkijuk.server.auth.dto.NaverTokenResponse;
import umc.kkijuk.server.auth.dto.NaverUserResponse;
import umc.kkijuk.server.auth.jwt.JwtUtil;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.member.emailauth.RedisService;
import umc.kkijuk.server.member.repository.MemberRepository;
import umc.kkijuk.server.member.service.MemberService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RestTemplate restTemplate;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
//    private final RedisService redisTokenService;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String kakaoGrantType;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String naverTokenUri;
    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String naverUserInfoUri;
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String naverGrantType;

//    @Transactional
//    public Map<String, Object> handleKakaoLogin(String code) {
//        // 1. 카카오 액세스 토큰 발급
//        String kakaoAccessToken = getKakaoAccessToken(code);
//
//        // 2. 카카오 사용자 정보 처리 및 사용자 생성/조회
//        Member member = processKakaoUser(kakaoAccessToken);
//
//        // 3. 사용자 상태 확인 및 활성화 처리
//        if (member.getUserState().equals(State.INACTIVATE)) {
//            member.activate();
//            memberRepository.save(member);
//        }
//        // 4. JWT 토큰 생성
//        Map<String, Object> tokens = new HashMap<>();
//        tokens.put("Token", generateTokens(member));
//
//        return tokens;
//    }

    @Transactional
    public Map<String, Object> handleKakaoLogin(String code) {
        // 1. 카카오 액세스 토큰 발급
        String kakaoAccessToken = getKakaoAccessToken(code);
        if (kakaoAccessToken == null || kakaoAccessToken.isEmpty()) {
            throw new IllegalArgumentException("카카오 액세스 토큰 발급 실패");
        }

        // 2. 카카오 사용자 정보 처리 및 사용자 생성/조회
        Member member = processKakaoUser(kakaoAccessToken);
        if (member == null) {
            throw new IllegalArgumentException("카카오 사용자 정보 처리 실패");
        }

        // 3. 사용자 상태 확인 및 활성화 처리
        if (member.getUserState().equals(State.INACTIVATE)) {
            member.activate();
            memberRepository.save(member);
        }

        // 4. JWT 토큰 생성
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("Token", generateTokens(member));
        return tokens;
    }

    @Transactional
    public Map<String, Object> handleNaverLogin(String code, String state) {
        String naverAccessToken = getNaverAccessToken(code, state);
        Member member = processNaverUser(naverAccessToken);

        if (member.getUserState().equals(State.INACTIVATE)) {
            member.activate();
            memberRepository.save(member);
        }

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("Token", generateTokens(member));
        return tokens;
    }

    @Transactional
    public String getKakaoAccessToken(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", kakaoGrantType);
        params.add("client_id", kakaoClientId);
        params.add("client_secret", kakaoClientSecret);
        params.add("redirect_uri", kakaoRedirectUri);
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

    @Transactional
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

    @Transactional
    public Member processKakaoUser(String accessToken) {
        Map<String, Object> kakaoUserInfo = getKakaoUserInfo(accessToken);
        String kakaoId = kakaoUserInfo.get("id").toString();
        String email = extractEmail(kakaoUserInfo);
        String name = extractName(kakaoUserInfo);
        String phoneNumber = extractPhoneNumber(kakaoUserInfo);
        LocalDate birthDate = extractBirthDate(kakaoUserInfo);

        log.info("카카오 사용자 정보 추출 - 이메일: {}, 이름: {}, 카카오 ID: {}, 전화번호: {}, 생년월일: {}", email, name, kakaoId, phoneNumber, birthDate);

        return memberRepository.findBySocialId(String.valueOf(kakaoId))
                .orElseGet(() -> {
                    log.info("신규 사용자 생성 - 카카오 ID: {}", kakaoId);
                    return memberService.createUserWithKakaoId(kakaoId, kakaoUserInfo);
                });
    }


    @Transactional
    public String getNaverAccessToken(String code, String state){
        String url = UriComponentsBuilder.fromHttpUrl(naverTokenUri)
                .queryParam("grant_type",naverGrantType)
                .queryParam("client_id", naverClientId)
                .queryParam("client_secret", naverClientSecret)
                .queryParam("code", code)
                .queryParam("state", state)
                .build().toUriString();

        try{
            ResponseEntity<NaverTokenResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    NaverTokenResponse.class
            );
            log.info("네이버 엑세스 토큰 응답: {}",response.getBody().getAccessToken());
            return response.getBody().getAccessToken();
        }catch (Exception e){
            log.error("네이버 엑세스 토큰 요청 실패 : {}",e.getMessage(),e);
            throw new RuntimeException("네이버 엑세스 토큰 요청 실패",e);
        }

    }
    public NaverUserResponse.NaverUserDetail getNaverUserInfo(String naverAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(naverAccessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<NaverUserResponse> response =
                    restTemplate.exchange(naverUserInfoUri, HttpMethod.GET, request, NaverUserResponse.class);
                log.info("네이버 사용자 정보 응답: {}", response.getBody());
            return response.getBody().getNaverUserDetail();

        } catch (Exception e) {
            log.error("네이버 사용자 정보 요청 실패: {}", e.getMessage());
            throw new RuntimeException("네이버 사용자 정보 요청 실패", e);

        }
    }

    public Member processNaverUser(String naverAccessToken) {
        NaverUserResponse.NaverUserDetail naverUserInfo = getNaverUserInfo(naverAccessToken);
        String naverId = naverUserInfo.getId ();
        String email = naverUserInfo.getEmail();
        String name = naverUserInfo.getName();
        String phoneNumber = naverUserInfo.getMobile();
        LocalDate birthDate = extractNaverBirthDate(naverUserInfo);

        log.info("네이버 사용자 정보 추출 - 이메일: {}, 이름: {}, 네이버 ID: {}, 전화번호: {}, 생년월일: {}", email, name, naverId, phoneNumber, birthDate);

        return memberRepository.findBySocialId(naverId)
                .orElseGet(() -> {
                    log.info("신규 사용자 생성 - 네이버 ID: {}", naverId);
                    return memberService.createUserWithNaverId(naverId,naverUserInfo);
                });
    }






    @Transactional
    public Map<String, String> generateTokens(Member member) {
        String kakaoId = String.valueOf(member.getSocialId());

        boolean isProfileComplete = member.getIsProfileComplete();

        String accessToken = jwtUtil.createAccessToken(kakaoId,isProfileComplete);
        String refreshToken = jwtUtil.createRefreshToken(kakaoId);

        log.info("JWT 토큰 생성 완료 - social ID: {}, accessToken: {}, refreshToken: {}", kakaoId, accessToken, refreshToken);

        member.setRefreshToken(refreshToken);
        memberRepository.save(member);
//        try {
//            boolean deleted = redisTokenService.deleteRefreshToken(kakaoId);
//            if (deleted) {
//                log.info("기존 리프레시 토큰 삭제 완료 - social ID: {}", kakaoId);
//            } else {
//                log.info("기존 리프레시 토큰이 존재하지 않음 - social ID: {}", kakaoId);
//            }
//        } catch (Exception e) {
//            log.warn("기존 리프레시 토큰 삭제 중 예외 발생 - social ID: {}", kakaoId, e);
//        }
//        redisTokenService.saveRefreshToken(kakaoId, refreshToken, 7 * 24 * 60 * 60 * 1000 );

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
    private LocalDate extractNaverBirthDate(NaverUserResponse.NaverUserDetail naverUserInfo) {
        String birthday = (String) naverUserInfo.getBirthday();
        String birthyear = (String) naverUserInfo.getBirthyear();
        log.info("Naver 생년월일 정보: {}, {}", birthday,birthyear );


        if (birthday == null || birthday.isEmpty()) {
            return null;
        }

        int year = (birthyear != null && !birthyear.isEmpty())
                ? Integer.parseInt(birthyear)
                : LocalDate.now().getYear();

        String[] dateParts = birthday.split("-");
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);


        return LocalDate.of(year, month, day);
    }


}
