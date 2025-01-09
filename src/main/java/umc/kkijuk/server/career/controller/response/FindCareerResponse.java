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
public class FindCareerResponse {
    private Long careerId;
    private String careerTitle;
    private String careerAlias;
    private LocalDate startdate;
    private LocalDate enddate;
    private CategoryResponse category;


    public FindCareerResponse(Long careerId, String careerTitle, String careerAlias,
                              LocalDate startdate, LocalDate enddate, CareerType type){
        this.careerId = careerId;
        this.careerTitle = careerTitle;
        this.careerAlias = careerAlias;
        this.startdate = startdate;
        this.enddate = enddate;
        this.category = new CategoryResponse(type.getId(),type.getDescription(),type.name());
    }

}
