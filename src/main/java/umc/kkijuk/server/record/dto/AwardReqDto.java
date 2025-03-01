package umc.kkijuk.server.record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardReqDto {
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String competitionName;
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String administer;
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String awardName;
    @NotEmpty(message = "입력하지 않은 항목이 있습니다.")
    private LocalDate acquireDate;
}
