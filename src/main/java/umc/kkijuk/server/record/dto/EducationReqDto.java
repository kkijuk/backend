package umc.kkijuk.server.record.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    private String category;
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String schoolName;
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String major;
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String state;
    @NotEmpty(message = "입력하지 않은 항목이 있습니다.")
    private LocalDate admissionDate;
    @NotEmpty(message = "입력하지 않은 항목이 있습니다.")
    private LocalDate graduationDate;
}
