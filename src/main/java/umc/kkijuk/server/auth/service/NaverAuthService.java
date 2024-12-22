package umc.kkijuk.server.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import umc.kkijuk.server.auth.dto.NaverTokenResponse;
import umc.kkijuk.server.auth.dto.NaverUserResponse;
import umc.kkijuk.server.auth.jwt.JwtUtil;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.emailauth.RedisService;
import umc.kkijuk.server.member.repository.MemberRepository;
import umc.kkijuk.server.member.service.MemberService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverAuthService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final RedisService redisTokenService;
    private final MemberService memberService;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String userInfoUri;
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String grantType;

    public String getNaverAccessToken(String code, String state){
        String url = UriComponentsBuilder.fromHttpUrl(tokenUri)
                .queryParam("grant_type",grantType)
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("state", state)
                .build().toUriString();


        ResponseEntity<NaverTokenResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, null, NaverTokenResponse.class);

        return response.getBody().getAccessToken();

    }

    public NaverUserResponse.NaverUserDetail getNaverUserInfo(String naverAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(naverAccessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<NaverUserResponse> response =
                restTemplate.exchange(userInfoUri, HttpMethod.GET, request, NaverUserResponse.class);

        return response.getBody().getNaverUserDetail();

    }
    public Member processNaverUser(String naverAccessToken) {
        NaverUserResponse.NaverUserDetail naverUserInfo = getNaverUserInfo(naverAccessToken);
        String naverId = naverUserInfo.getId ();
        String email = naverUserInfo.getEmail();
        String name = naverUserInfo.getName();
        String phoneNumber = naverUserInfo.getMobile();
        LocalDate birthDate = extractBirthDate(naverUserInfo);

        log.info("네이버 사용자 정보 추출 - 이메일: {}, 이름: {}, 네이버 ID: {}, 전화번호: {}, 생년월일: {}", email, name, naverId, phoneNumber, birthDate);

        return memberRepository.findBySocialId(naverId)
                .orElseGet(() -> {
                    log.info("신규 사용자 생성 - 네이버 ID: {}", naverId);
                    return memberService.createUserWithNaverId(naverId,naverUserInfo);
                });
    }

    public Object generateTokens(Member member) {
        String naverId = String.valueOf(member.getSocialId());
        String accessToken = jwtUtil.createAccessToken(naverId);
        String refreshToken = jwtUtil.createRefreshToken(naverId);

        log.info("JWT Token 생성 완료 - 네이버 ID : {}, accessToken : {}, refreshToken : {}", naverId, accessToken, refreshToken);

        redisTokenService.saveRefreshToken(naverId, refreshToken, 7 * 24 * 60 * 60 * 1000 );

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    public LocalDate extractBirthDate(NaverUserResponse.NaverUserDetail naverUserInfo) {
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
