package umc.kkijuk.server.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.member.domain.MarketingAgree;
import umc.kkijuk.server.member.domain.MemberJob;

import java.util.List;

@Data
@NoArgsConstructor
public class ProfileInputDto {

    @NotNull(message = "서비스 약관 동의 여부는 필수 값입니다.")
    @Schema(description = "서비스 약관 동의 여부", example = "true", type = "boolean")
    private Boolean isTermsAgreed;

    @NotNull(message = "개인정보 처리 방침 동의 여부는 필수 값입니다.")
    @Schema(description = "개인정보 처리 방침 동의 여부", example = "true", type = "boolean")
    private Boolean isPrivacyAgreed;

    @NotNull(message = "마케팅 정보 수신 동의 여부는 필수 값입니다.")
    @Schema(description = "마케팅 정보 수신 동의 여부", example = "BOTH", type = "string",allowableValues = {
            "BOTH",
            "EMAIL",
            "SMS",
            "NONE" })
    private MarketingAgree isMarketingAgreed;

    @NotNull(message = "회원 직업 정보는 필수 값입니다.")
    @Schema(description = "회원 직업 정보", example = "JOB_SEEKER", type = "array", allowableValues = {
            "MIDDLE_OR_HIGH_SCHOOL", // 중/고등학생
            "JOB_SEEKER",            // 취준생
            "UNIVERSITY_STUDENT",    // 대학 재/휴학생
            "UNIVERSITY_GRADUATE",   // 대학 졸업(유예)생
            "EMPLOYEE",              // 직장인
            "FREELANCER",            // 프리랜서
            "ENTREPRENEUR",          // 창업/사업 중
            "OTHER"     })
    private List<MemberJob> memberJob;
}

