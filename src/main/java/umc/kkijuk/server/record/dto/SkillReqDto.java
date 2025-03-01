package umc.kkijuk.server.record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.record.domain.SkillTag;
import umc.kkijuk.server.record.domain.Workmanship;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SkillReqDto {
    @NotNull(message = "입력하지 않은 항목이 있습니다.")
    private SkillTag skillTag;
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String skillName;
    @NotNull(message = "입력하지 않은 항목이 있습니다.")
    private Workmanship workmanship;
}
