package umc.kkijuk.server.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileReqDto {
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    @Size(max = 20)
    @Schema(description = "첨부파일 제목", example = "이력서 최종", type="string")
    private String title;

    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    @Schema(description = "파일 keyName",type="string")
    private String keyName;
}
