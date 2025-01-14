package umc.kkijuk.server.auth.dto;

import lombok.Getter;
import umc.kkijuk.server.member.domain.Member;

@Getter
public class OAuth2ResponseImpl implements OAuth2Response {
  private final String provider = "kakao"; // Provider 고정 (예: Kakao)
  private final String providerId;
  private final String email;

  public OAuth2ResponseImpl(String providerId, String email) {
    this.providerId = providerId;
    this.email = email;
  }

  public static OAuth2ResponseImpl fromUser(Member member) {
    return new OAuth2ResponseImpl(
          member.getId().toString(), // 예시로 user ID를 providerId로 설정
          member.getEmail()
        );
  }
}
