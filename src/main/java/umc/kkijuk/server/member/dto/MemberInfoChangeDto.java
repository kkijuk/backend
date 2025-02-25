package umc.kkijuk.server.member.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.member.domain.MarketingAgree;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MemberInfoChangeDto {
    @NotNull
    private String email;

    @NotBlank(message = "연락처를 입력해주세요.")
    @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$", message = "올바른 연락처를 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birthDate;

    @NotNull
    private MarketingAgree marketingAgree;

    @Builder
    public MemberInfoChangeDto(String email,String phoneNumber, LocalDate birthDate, MarketingAgree marketingAgree) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.marketingAgree = marketingAgree;
    }
}
