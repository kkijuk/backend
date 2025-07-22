package umc.kkijuk.server.detail.controller.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import umc.kkijuk.server.career.controller.response.CategoryResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "최근 활동 기록 응답 DTO")
public class RecentCareerDetailResponse {

  @Schema(description = "활동 기록 ID", example = "123")
  private Long detailId;
  @Schema(description = "활동 기록 제목", example = "끼적 백엔드")
  private String detailTitle;
  @Schema(description = "활동 기록 내용", example = "끼적 api 개발~..")
  private String detailContent;
  @Schema(description = "활동 기록 시작일", example = "2025-05-01")
  private LocalDate detailStartDate;
  @Schema(description = "활동 기록 종료일", example = "2025-06-30")
  private LocalDate detailEndDate;
  @Schema(description = "활동 기록에 연결된 태그 목록")
  private List<TagResponse> tags;

  @Schema(description = "활동 ID", example = "77")
  private Long careerId;
  @Schema(description = "활동 제목", example = "대외활동 - 백엔드")
  private String careerTitle;
  @Schema(description = "활동 별칭", example = "스프링부트")
  private String careerAlias;
  @Schema(description = "활동 카테고리 정보 (id, 설명, name)")
  private CategoryResponse category;
}

