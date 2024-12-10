package umc.kkijuk.server.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthService {

    private final RestTemplate restTemplate;

    public Map<String, Object> getKakaoUserInfo(String accessToken) {
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            log.info("카카오 사용자 정보 요청: URL={}, Headers={}", userInfoUri, headers);
            ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, Map.class);
            log.info("카카오 사용자 정보 응답: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.error("카카오 사용자 정보 요청 실패: {}", e.getMessage());
            throw new RuntimeException("카카오 사용자 정보 요청 실패", e);
        }
    }

    public String extractEmail(Map<String, Object> kakaoUserInfo) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUserInfo.get("kakao_account");
        return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
    }

    public String extractName(Map<String, Object> kakaoUserInfo) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) ((Map<String, Object>) kakaoUserInfo.get("kakao_account")).get("profile");
        return kakaoAccount != null ? (String) kakaoAccount.get("name") : null;
    }

    public String extractPhoneNumber(Map<String, Object> kakaoUserInfo) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUserInfo.get("kakao_account");
        return kakaoAccount != null ? (String) kakaoAccount.get("phone_number") : null;
    }

    public LocalDate extractBirthDate(Map<String, Object> kakaoUserInfo) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUserInfo.get("kakao_account");
        if (kakaoAccount == null) return null;

        String birthday = (String) kakaoAccount.get("birthday"); // MMDD 형식
        String birthyear = (String) kakaoAccount.get("birthyear"); // YYYY 형식 (선택적)

        if (birthday == null || birthday.isEmpty()) {
            return null; // 생일 정보가 없는 경우
        }

        // 출생 연도가 없는 경우 현재 연도로 설정
        int year = (birthyear != null && !birthyear.isEmpty())
                ? Integer.parseInt(birthyear) // 출생 연도 사용
                : LocalDate.now().getYear(); // 기본적으로 현재 연도 사용

        // MMDD → 월, 일 추출
        int month = Integer.parseInt(birthday.substring(0, 2));
        int day = Integer.parseInt(birthday.substring(2, 4));

        return LocalDate.of(year, month, day); // LocalDate로 변환
    }

}
