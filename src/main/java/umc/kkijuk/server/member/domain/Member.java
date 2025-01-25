package umc.kkijuk.server.member.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import umc.kkijuk.server.common.converter.MemberJobListConverter;
import umc.kkijuk.server.common.domian.base.BaseEntity;
import umc.kkijuk.server.common.converter.StringListToStringConverter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @NotNull
    private String email;

//    @NotNull
    private String name;

//    @NotNull
    private String phoneNumber;

//    @NotNull
    private LocalDate birthDate;

    @Convert(converter = StringListToStringConverter.class)
    private List<String> field;

    //소셜 로그인 후 추가적으로 입력받아야 하는 항목들 - 4개
//    @NotNull
    @Enumerated(EnumType.STRING)
    private MarketingAgree marketingAgree; //마케팅 정보 수신 동의 여부
    private Boolean termsAgree; //이용약관 동의 여부
    private Boolean privacyAgree; //개인정보 수집 동의 여부

//    @Column(name = "member_job", columnDefinition = "TEXT")
    @Convert(converter = StringListToStringConverter.class)
    private List<String> memberJob;

    //그리고 사용자가 4개의 정보를 입력하였는지를 확인할 수 있는 상태
    private Boolean isProfileComplete;

//    @NotNull
    @Enumerated(EnumType.STRING)
    private State userState;

    private LocalDate deleteDate;

    @Convert(converter = StringListToStringConverter.class)
    private List<String> recruitTags;


//    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    public Member(String email, String name, String phoneNumber, LocalDate birthDate, MarketingAgree marketingAgree, State userState) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.marketingAgree = marketingAgree;
        this.userState = userState;
    }

    public void changeFieldInfo(List<String> field){
        this.field = field;
    }

    public void changeMemberInfo(String phoneNumber, LocalDate birthDate, MarketingAgree marketingAgree){
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.marketingAgree = marketingAgree;
    }

    public void inactivate() {
        this.userState = State.INACTIVATE;
        this.deleteDate = LocalDate.now().plusWeeks(1);
    }

    public void activate() {
        this.userState = State.ACTIVATE;
        this.deleteDate = null;
    }

    public void addRecruitTag(String tag){
        this.recruitTags.add(tag);
    }

    public void deleteRecruitTag(String tag) {
        this.recruitTags.remove(tag);
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public void setEmail(String email) {this.email = email;}

    public void setName(String name) {this.name = name;}
    public void setRole(Role role) {this.role = role;}

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
//    public void setRefreshToken(String refreshToken){ this.refreshToken = refreshToken;}
    public void setSocialType(SocialType type){this.socialType = type;}
    public void setUserState(State state){this.userState = state;}

    public void setMarketingAgree(MarketingAgree marketingAgree) {
        this.marketingAgree = marketingAgree;
    }

    public void setTermsAgree(Boolean termsAgree) {
        this.termsAgree = termsAgree;
    }

    public void setPrivacyAgree(Boolean privacyAgree) {
        this.privacyAgree = privacyAgree;
    }

    public void setMemberJob(List<String> memberJob) {
        this.memberJob = memberJob;
    }

    public void setProfileComplete(Boolean profileComplete) {
        isProfileComplete = profileComplete;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
