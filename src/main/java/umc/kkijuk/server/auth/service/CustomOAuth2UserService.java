package umc.kkijuk.server.auth.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import umc.kkijuk.server.auth.dto.CustomOAuth2User;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.Role;
import umc.kkijuk.server.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    Map<String, Object> attributes = oAuth2User.getAttributes();
    log.info("OAuth2User Attributes: {}", attributes);

    // 이메일 및 닉네임 추출
    String email = extractEmail(attributes);
    String nickname = extractNickname(attributes);

    log.info("Extracted email: {}", email);
    log.info("Extracted nickname: {}", nickname);

    if (email == null || email.isEmpty()) {
      throw new OAuth2AuthenticationException("Email is required but not provided.");
    }

    Member member = memberRepository.findByEmail(email).orElseGet(() -> createNewUser(email, nickname));

    // OAuth2User 객체 생성
    return createCustomOAuth2User(member, attributes);
  }

  private OAuth2User createCustomOAuth2User(Member member, Map<String, Object> attributes) {
    Map<String, Object> updatedAttributes = new HashMap<>(attributes);
    updatedAttributes.put("email", member.getEmail()); // 필수 속성 추가

    return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            updatedAttributes,
            "email" // 기본 속성 키 설정
    );
  }

  private String extractEmail(Map<String, Object> attributes) {
    if (attributes.containsKey("kakao_account")) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
      return (String) kakaoAccount.get("email");
    }
    return null;
  }

  private String extractNickname(Map<String, Object> attributes) {
    if (attributes.containsKey("properties")) {
      Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
      return (String) properties.get("nickname");
    }
    return null;
  }

  private Member createNewUser(String email, String name) {
    Member member = new Member();
    member.setEmail(email);
    member.setName(name);
    memberRepository.save(member);

    log.info("New user created and saved in DB. Email: {}, Nickname: {}", email, name);
    return member;
  }


}
