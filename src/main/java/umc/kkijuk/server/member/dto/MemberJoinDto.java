//package umc.kkijuk.server.member.dto;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotNull;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import umc.kkijuk.server.member.domain.MarketingAgree;
//import umc.kkijuk.server.member.domain.Member;
//import umc.kkijuk.server.member.domain.State;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//public class MemberJoinDto {
//
//    @NotNull @Email
//    @Schema(description = "이메일", example = "test@gmail.com", type = "string")
//    private String email;
//    @NotNull
//    private String name;
//    @NotNull
//    private String phoneNumber;
//    @NotNull
//    private LocalDate birthDate;
//    @NotNull
//    private String password;
//    @NotNull
//    private String passwordConfirm;
//    @NotNull
//    private MarketingAgree marketingAgree;
//    @NotNull
//    private State userState;
//
////    @Builder
////    public MemberJoinDto(String email, String name, String phoneNumber, LocalDate birthDate,
////                         String password, Boolean marketingAgree, State userState) {
////        this.email = email;
////        this.name = name;
////        this.phoneNumber = phoneNumber;
////        this.birthDate = birthDate;
////        this.password = password;
////        this.marketingAgree = marketingAgree;
////        this.userState = userState;
////    }
//
//    @Builder
//    public MemberJoinDto(String email, String name, String phoneNumber, LocalDate birthDate,
//                         String password, String passwordConfirm, MarketingAgree marketingAgree, State userState) {
//        this.email = email;
//        this.name = name;
//        this.phoneNumber = phoneNumber;
//        this.birthDate = birthDate;
//        this.password = password;
//        this.passwordConfirm = passwordConfirm;
//        this.marketingAgree = marketingAgree;
//        this.userState = userState;
//    }
//
//    public Member toEntity() {
//        return Member.builder()
//                .email(email)
//                .name(name)
//                .phoneNumber(phoneNumber)
//                .birthDate(birthDate)
//                .password(password)
//                .marketingAgree(marketingAgree)
//                .userState(userState)
//                .recruitTags(new ArrayList<>(List.of("인턴", "정규직", "대외활동", "동아리")))
//                .build();
//    }
//
//}
