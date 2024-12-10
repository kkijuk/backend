package umc.kkijuk.server.auth.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

  private final Map<String, Object> attributes; // 전체 응답 데이터
  private final Map<String, Object> kakaoAccountAttributes; // 카카오 계정 데이터
  private final Map<String, Object> profileAttributes; // 프로필 데이터

  // 생성자
  public KakaoResponse(Map<String, Object> attributes) {
    this.attributes = attributes;
    this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
    this.profileAttributes =
        kakaoAccountAttributes != null
            ? (Map<String, Object>) kakaoAccountAttributes.get("profile")
            : null;
  }

  @Override
  public String getProvider() {
    return "kakao"; // 고정 값
  }

  @Override
  public String getProviderId() {
    return attributes.get("id").toString(); // 카카오 사용자 고유 ID
  }

  @Override
  public String getEmail() {
    return kakaoAccountAttributes != null && kakaoAccountAttributes.containsKey("email")
        ? kakaoAccountAttributes.get("email").toString()
        : null; // 이메일이 없는 경우 null 반환
  }

}
