package umc.kkijuk.server.career.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.detail.domain.CareerType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareerSummaryReqDto {
    @NotNull(message = "활동의 유형을 나타내는 타입은 필수 입력 항목입니다.")
    @Schema(description = "활동의 유형을 나타내는 타입", example = "ACTIVITY", type = "string", allowableValues = {"ACTIVITY", "PROJECT", "EDU", "EMP","CIRCLE","COM"})
    private CareerType type;

    @Size(max = 500)
    @Schema(description = "활동 내역", example = "주요 활동 내용을 요약하여 작성해주세요. 최대 500자 까지 입력 가능 선택사항입니다.", type = "string")
    private String summary;
}
