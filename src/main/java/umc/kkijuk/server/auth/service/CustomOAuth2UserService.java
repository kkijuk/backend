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

    // 전체 응답 데이터 로그 출력
    Map<String, Object> attributes = oAuth2User.getAttributes();
    log.info("OAuth2User Attributes: {}", attributes);

    // 사용자 정보 추출
    String email = extractEmail(attributes);
    String name = extractName(attributes);
    String phoneNumber = extractPhoneNumber(attributes);
    LocalDate birthDate = extractBirthDate(attributes);

    log.info("Extracted user info: email={}, name={}, phoneNumber={}, birthDate={}",
            email, name, phoneNumber, birthDate);

    if (email == null || email.isEmpty()) {
      throw new OAuth2AuthenticationException("Email is required but not provided.");
    }

    // 사용자 조회 또는 신규 생성
    Member member = memberRepository.findByEmail(email).orElseGet(() -> createNewMember(email, name, phoneNumber, birthDate));

    // OAuth2User 객체 생성
    return createCustomOAuth2User(member, attributes);
  }

  private OAuth2User createCustomOAuth2User(Member member, Map<String, Object> attributes) {
    Map<String, Object> updatedAttributes = new HashMap<>(attributes);
    updatedAttributes.put("email", member.getEmail()); // 필수 속성 추가
    updatedAttributes.put("name", member.getName());
    updatedAttributes.put("phoneNumber", member.getPhoneNumber());
    updatedAttributes.put("birthDate", member.getBirthDate().toString());

    return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())), // Role에 따라 권한 설정
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

  private String extractName(Map<String, Object> attributes) {
    if (attributes.containsKey("kakao_account")) {
      Map<String, Object> profile = (Map<String, Object>) ((Map<String, Object>) attributes.get("kakao_account")).get("profile");
      return profile != null ? (String) profile.get("name") : null;
    }
    return null;
  }

  private String extractPhoneNumber(Map<String, Object> attributes) {
    if (attributes.containsKey("kakao_account")) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
      return kakaoAccount != null ? (String) kakaoAccount.get("phone_number") : null;
    }
    return null;
  }

  private LocalDate extractBirthDate(Map<String, Object> attributes) {
    if (attributes.containsKey("kakao_account")) {
      Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
      if (kakaoAccount == null) return null;

      String birthday = (String) kakaoAccount.get("birthday"); // MMDD 형식
      String birthyear = (String) kakaoAccount.get("birthyear"); // YYYY 형식 (선택적)

      if (birthday == null || birthday.isEmpty()) {
        return null; // 생일 정보가 없는 경우
      }

      int year = (birthyear != null && !birthyear.isEmpty())
              ? Integer.parseInt(birthyear)
              : LocalDate.now().getYear();

      int month = Integer.parseInt(birthday.substring(0, 2));
      int day = Integer.parseInt(birthday.substring(2, 4));

      return LocalDate.of(year, month, day);
    }
    return null;
  }

  private Member createNewMember(String email, String name, String phoneNumber, LocalDate birthDate) {
    Member member = Member.builder()
            .email(email)
            .name(name != null ? name : "Default User") // 이름이 없는 경우 기본값 설정
            .phoneNumber(phoneNumber != null ? phoneNumber : "Unknown") // 핸드폰 번호 기본값 설정
            .birthDate(birthDate != null ? birthDate : LocalDate.now()) // 생일이 없는 경우 현재 날짜로 설정
            .role(Role.ROLE_USER) // 기본 권한 설정
            .build();

    memberRepository.save(member);
    log.info("New member created and saved in DB. Email: {}, Name: {}, Phone: {}, BirthDate: {}",
            email, name, phoneNumber, birthDate);
    return member;
  }
}
