package umc.kkijuk.server.career.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(title="CategoryResponse : 내 커리어 카테고리 DTO")
public class CategoryResponse {
    private int categoryId;
    private String categoryKoName;
    private String categoryEnName;

}
