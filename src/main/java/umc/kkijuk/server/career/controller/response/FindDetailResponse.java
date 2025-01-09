package umc.kkijuk.server.career.controller.response;

import lombok.*;
import umc.kkijuk.server.career.domain.BaseCareer;
import umc.kkijuk.server.detail.controller.response.BaseCareerDetailResponse;
import umc.kkijuk.server.detail.domain.CareerType;


import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class FindDetailResponse implements BaseCareerResponse {
    private Long careerId;
    private CategoryResponse category;
    private String careerTitle;
    private String careerAlias;
    private LocalDate startdate;
    private LocalDate enddate;
    private List<BaseCareerDetailResponse> detailList;

    @Override
    public LocalDate getEndDate() {
        return this.enddate;
    }

    public FindDetailResponse(Long careerId, String careerTitle, String careerAlias, LocalDate startdate, LocalDate enddate,
                              List<BaseCareerDetailResponse> detailList, CareerType type){
        this.careerId = careerId;
        this.careerTitle = careerTitle;
        this.careerAlias = careerAlias;
        this.startdate = startdate;
        this.enddate = enddate;
        this.detailList = detailList;
        this.category = new CategoryResponse(type.getId(),type.getDescription(),type.name());
    }
}
