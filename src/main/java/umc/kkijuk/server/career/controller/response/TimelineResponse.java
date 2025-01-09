package umc.kkijuk.server.career.controller.response;

import lombok.*;
import umc.kkijuk.server.career.domain.BaseCareer;
import umc.kkijuk.server.detail.domain.CareerType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class TimelineResponse {
    private Long careerId;
    private CategoryResponse category;
    private String title;
    private LocalDate startdate;
    private LocalDate enddate;
    public TimelineResponse(BaseCareer career, CareerType type){
        this.careerId = career.getId();
        this.title = career.getName();
        this.startdate = career.getStartdate();
        this.enddate = career.getEnddate();
        this.category = new CategoryResponse(type.getId(),type.getDescription(),type.name());
    }
}
