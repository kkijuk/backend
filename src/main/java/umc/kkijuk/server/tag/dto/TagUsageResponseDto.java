package umc.kkijuk.server.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagUsageResponseDto {
  private Long id;
  private String name;
  private long usageCount;

}
