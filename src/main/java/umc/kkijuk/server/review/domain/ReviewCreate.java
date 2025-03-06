package umc.kkijuk.server.review.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewCreate {

    @NotBlank(message = "공고 후기 제목은 필수 입력 항목입니다.")
    @Schema(description = "공고 후기 제목", example = "코딩 테스트", type = "string")
    private String title;

    @Schema(description = "공고 후기 내용", example = "Dijkstra 알고리즘을 활용하는 문제", type = "string")
    @Size(max = 1000, message = "공고 후기 내용은 1000자 이내로 작성해주세요.")
    private String content;

    @NotNull(message = "날짜는 필수 입력 항목입니다.")
    @Schema(description = "날짜", example = "2024-07-21", pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;
}
