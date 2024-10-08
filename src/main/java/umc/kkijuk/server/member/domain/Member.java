package umc.kkijuk.server.member.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import umc.kkijuk.server.career.domain.base.BaseEntity;
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

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private String password;

    @Convert(converter = StringListToStringConverter.class)
    private List<String> field;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MarketingAgree marketingAgree;

    @NotNull
    @Enumerated(EnumType.STRING)
    private State userState;

    private LocalDate deleteDate;

    @Convert(converter = StringListToStringConverter.class)
    private List<String> recruitTags;


    public Member(String email, String name, String phoneNumber, LocalDate birthDate, String password, MarketingAgree marketingAgree, State userState) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.password = password;
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

    public void changeMemberPassword(String password){
        this.password = password;
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
}
