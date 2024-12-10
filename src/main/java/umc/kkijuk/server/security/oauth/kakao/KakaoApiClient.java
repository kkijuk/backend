//package umc.kkijuk.server.security.oauth.kakao;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Objects;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class KakaoApiClient {
//    @Value("${kakao.rest_api.oauth.url.auth}")
//    private String authUrl;
//    @Value("${kakao.rest_api.oauth.url.api}")
//    private String apiUrl;
//    @Value("${kakao.rest_api.key}")
//    private String clientId;
//    @Value("${kakao.rest_api.oauth.client-secret}")
//    private String clientSecret;
//
//    private final RestTemplate restTemplate;
//
//    public String requestAccessToken(KakaoLoginParam params) {
//        String url = authUrl + "/oauth/token";
//        HttpEntity<MultiValueMap<String, String>> request = generateHttpRequest(params);
//
//        KakaoToken kakaoToken = restTemplate.postForObject(url, request, KakaoToken.class);
//        Objects.requireNonNull(kakaoToken, "Kakao token is null");
//
//        return kakaoToken.accessToken();
//    }
//
//    public KakaoUserInfo requestOAuthInfo(String accessToken) {
//        String url = apiUrl + "/v2/user/me";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set("Authorization", "Bearer " + accessToken);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("property_keys", "[\"kakao_account.email\"]");
//
//        HttpEntity<?> request = new HttpEntity<>(body, headers);
//
//        return restTemplate.postForObject(url, request, KakaoUserInfo.class);
//    }
//
//    private HttpEntity<MultiValueMap<String, String>> generateHttpRequest(KakaoLoginParam params) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = params.makeBody();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
//
//        return new HttpEntity<>(body, headers);
//    }
//}
