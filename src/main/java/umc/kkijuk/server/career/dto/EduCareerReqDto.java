package umc.kkijuk.server.career.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//교육

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EduCareerReqDto {
    @NotBlank(message = "교육명은 필수 입력 항목입니다. 최대 20자 까지 입력 가능")
    @Size(max = 20)
    @Schema(description = "교육명", example = "스프링부트 입문 강의 수강", type="string")
    private String name;

    @NotBlank(message = "별칭은 필수 입력 항목입니다. 최대 20자 까지 입력 가능")
    @Size(max = 20)
    @Schema(description = "별칭", example = "스프링부트 입문 강의", type="string")
    private String alias;

    @NotNull(message = "활동 기간을 알고 있는지 여부를 나타냅니다.")
    @Schema(description = "활동 기간 인지 여부", example = "false", type = "boolean")
    private Boolean unknown;

    @NotNull(message = "활동 시작 날짜는 필수 입력 항목입니다.")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "활동 시작 날짜", example = "2024-04-14", type="string")
    private LocalDate startdate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "활동 종료 날짜", example = "2024-07-20", type = "string")
    private LocalDate enddate;


    //교육 입력 사항들
    @NotBlank(message = "주최는 필수 입력 항목입니다. 최대 15자 까지 입력 가능")
    @Size(max = 15)
    @Schema(description = "주최", example = "인프런", type="string")
    private String organizer;

    @NotNull(message = "교육시간는 필수 입력 항목입니다. 최대 4자리까지 숫자 입력 가능")
    @Schema(description = "4자리까지 직접 입력 가능", example = "126", type = "int")
    @Max(9999)
    @Min(0)
    private Integer time;
}
