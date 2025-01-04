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

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityReqDto {
    @NotBlank(message = "활동명은 필수 입력 항목입니다. 최대 20자 까지 입력 가능")
    @Size(max = 20)
    @Schema(description = "활동명", example = "대외활동", type="string")
    private String name;

    @NotBlank(message = "활동 별칭은 필수 입력 항목입니다. 최대 20자 까지 입력 가능")
    @Size(max = 20)
    @Schema(description = "활동 별칭", example = "연합동아리", type="string")
    private String alias;

    @NotNull(message = "활동 기간을 알고 있는지 여부를 나타냅니다.")
    @Schema(description = "활동 기간 인지 여부", example = "false", type = "boolean")
    private Boolean unknown;

//    @Size(max = 200)
//    @Schema(description = "활동 내역", example = "주요 활동 내용을 요약하여 작성해주세요. 최대 50자 까지 입력 가능 선택사항입니다.", type = "string")
//    private String summary;

    @NotNull(message = "활동 시작 날짜는 필수 입력 항목입니다.")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "활동 시작 날짜", example = "2024-04-14", type="string")
    private LocalDate startdate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "활동 종료 날짜", example = "2024-07-20", type = "string")
    private LocalDate enddate;

    @NotBlank(message = "주최는 필수 입력 항목입니다. 최대 15자 까지 입력 가능")
    @Size(max = 15)
    @Schema(description = "주최", example = "컴공선배", type="string")
    private String organizer;

    @Size(max = 15)
    @Schema(description = "역할. 최대 15자까지 직접 입력 가능합니다.", example = "동아리 회장", type = "String")
    private String role;

    @NotNull(message = "팀일 경우 true, 팀이 아닐 경우 false")
    @Schema(description = "팀/개인 여부", example = "false", type = "boolean")
    private Boolean isTeam;

    //팀 선택시 입력 사항인
    @Schema(description = "인원, 숫자만, 2자리까지 직접 입력 가능", example = "30", type = "int")
    private Integer teamSize;
    @Schema(description = "기여도, 숫자만, 100이내 직접 입력 가능", example = "20", type = "int")
    private Integer contribution;
}
