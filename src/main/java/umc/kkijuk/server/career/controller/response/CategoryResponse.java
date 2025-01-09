package umc.kkijuk.server.career.controller.response;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryResponse {
    private int categoryId;
    private String categoryKoName;
    private String categoryEnName;

}
