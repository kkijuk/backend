package umc.kkijuk.server.record.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FileRenameDto {
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String oldFileName;
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String newFileName;

}
