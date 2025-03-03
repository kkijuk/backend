package umc.kkijuk.server.record.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Year;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EducationReqDto {

    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    @Schema(description = "학력 구분", example = "재학", type="string")
    private String category;

    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    @Schema(description = "학교 이름", example = "끼적대학교", type="string")
    private String schoolName;

    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    @Schema(description = "전공", example = "컴퓨터공학과", type="string")
    private String major;

    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    @Schema(description = "학력상태", example = "휴학", type="string")
    private String state;

    @NotNull(message = "입력하지 않은 항목이 있습니다.")
    @JsonFormat(pattern = "yyyy-MM")
    @Schema(type = "string", example = "2021-03")
    private YearMonth admissionDate;

    @NotNull(message = "입력하지 않은 항목이 있습니다.")
    @JsonFormat(pattern = "yyyy-MM")
    @Schema(type = "string", example = "2025-08")
    private YearMonth graduationDate;
}
