package umc.kkijuk.server.record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UrlReqDto {
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    private String urlTitle;
    @NotBlank(message = "입력하지 않은 항목이 있습니다.")
    @URL(message = "유효한 URL 형식이 아닙니다.")
    private String url;
}
