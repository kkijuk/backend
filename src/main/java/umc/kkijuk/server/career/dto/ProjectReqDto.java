package umc.kkijuk.server.career.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.career.domain.ProjectType;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectReqDto {
    @NotBlank(message = "활동명은 필수 입력 항목입니다. 최대 20자 까지 입력 가능")
    @Size(max = 20)
    @Schema(description = "활동명", example = "웹 프로젝트", type="string")
    private String name;

    @NotBlank(message = "활동 별칭은 필수 입력 항목입니다. 최대 20자 까지 입력 가능")
    @Size(max = 20)
    @Schema(description = "활동 별칭", example = "끼적", type="string")
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

    //프로젝트 필수 입력사항
    @NotNull(message = "프로젝트 소속은 필수 입력 항목입니다.")
    @Schema(description = "프로젝트 소 ", example = "OTHER", type = "string", allowableValues = {"ON_CAMPUS", "OFF_CAMPUS", "OTHER"})
    private ProjectType location;

    @NotNull(message = "팀일 경우 true, 팀이 아닐 경우 false")
    @Schema(description = "팀/개인 여부", example = "false", type = "boolean")
    private Boolean isTeam;

    //팀 선택시 입력 사항 ( 선택 )
    @Schema(description = "인원, 숫자만, 2자리까지 직접 입력 가능", example = "4", type = "int")
    private Integer teamSize;
    @Schema(description = "기여도, 숫자만, 100이내 직접 입력 가능", example = "80", type = "int")
    private Integer contribution;

}
