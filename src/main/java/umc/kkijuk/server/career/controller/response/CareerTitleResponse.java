package umc.kkijuk.server.career.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "활동 요약 정보 응답 DTO")
public class CareerTitleResponse {

  @Schema(description = "활동 ID", example = "42")
  private Long careerId;
  @Schema(description = "카테고리 정보 (id, 설명, name)")
  private CategoryResponse category;
  @Schema(description = "활동 제목", example = "끼적 백엔드 개발")
  private String title;
  @Schema(description = "활동 별칭", example = "Spring Boot API")
  private String alias;
}
