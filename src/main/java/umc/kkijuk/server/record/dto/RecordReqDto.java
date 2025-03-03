package umc.kkijuk.server.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecordReqDto {
    @Schema(description = "주소", example = "서울특별시 중구 세종대로 110", type="string")
    private String address;

    @Schema(description = "사진 주소", example = "testtestimageaddress", type="string")
    private String profileImageUrl;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    @Schema(description = "이메일", example = "kkijuk@gmail.com", type="string")
    private String email;
}
