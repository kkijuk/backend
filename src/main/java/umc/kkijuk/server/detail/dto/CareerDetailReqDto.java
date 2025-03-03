package umc.kkijuk.server.detail.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import umc.kkijuk.server.detail.domain.CareerType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CareerDetailReqDto {
    @NotNull(message = "활동 유형은 필수 입력 항목입니다.")
    @Schema(description = "활동 유형", example = "activity", type = "string", allowableValues = {"activity", "project", "edu", "employment","competition","circle","etc"})
    private String careerType;

    @Size(max = 30)
    @NotBlank(message = "활동 기록 제목은 필수 입력 항목입니다. 최대 30자 까지 입력 가능")
    @Schema(description = "활동 기록 제목", example = "아이디어톤", type="string")
    private String title;

    @Size(max = 800)
    @NotBlank(message = "활동 기록 내용은 필수 입력 항목입니다. 최대 800자 까지 입력 가능")
    @Schema(description = "활동 기록 내용", example = "기획한 웹/앱 서비스를 발표하고 피드백을 교환함"+"투표 결과 우수상 수상", type="string")
    private String content;

    @NotNull(message = "활동 시작 날짜는 필수 입력 항목입니다.")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "활동 기록 시작 날짜", example = "2024-04-14", type="string")
    private LocalDate startDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Schema(description = "활동 기록 종료 날짜", example = "2024-04-14", type="string")
    private LocalDate endDate;

    @Schema(description = "태그 리스트")
    private List<Long> tagList;
}
