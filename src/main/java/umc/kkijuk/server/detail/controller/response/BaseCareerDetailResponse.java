package umc.kkijuk.server.detail.controller.response;

import lombok.*;
import umc.kkijuk.server.career.controller.response.CategoryResponse;
import umc.kkijuk.server.detail.domain.BaseCareerDetail;
import umc.kkijuk.server.detail.domain.CareerType;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BaseCareerDetailResponse {
//    private CategoryResponse category;
    private Long detailId;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<TagResponse> detailTag;

    public BaseCareerDetailResponse(BaseCareerDetail detail) {
        this.detailId = detail.getId();
        this.title = detail.getTitle();
        this.content = detail.getContent();
        this.startDate = detail.getStartDate();
        this.endDate = detail.getEndDate();
//        this.category = new CategoryResponse(type.getId(),type.getDescription(),type.name());
        this.detailTag = Optional.ofNullable(detail.getCareerTagList())
                .orElse(Collections.emptyList())
                .stream()
                .map(careerDetailTag -> new TagResponse(careerDetailTag.getTag()))
                .collect(Collectors.toList());
    }

}
