package umc.kkijuk.server.record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.record.domain.LicenseTag;

import java.time.LocalDate;
import java.time.YearMonth;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LicenseReqDto {
    @NotEmpty(message = "입력하지 않은 항목이 있습니다.")
    private LicenseTag licenseTag;
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String licenseName;
    private String administer;
    private String licenseNumber;
    private String licenseGrade;
    @NotNull(message = "입력하지 않은 항목이 있습니다.")
    private LocalDate acquireDate;
}
