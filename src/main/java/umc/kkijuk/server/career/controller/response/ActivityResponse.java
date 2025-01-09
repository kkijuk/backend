package umc.kkijuk.server.career.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import umc.kkijuk.server.career.domain.Activity;
import umc.kkijuk.server.detail.controller.response.BaseCareerDetailResponse;
import umc.kkijuk.server.detail.domain.BaseCareerDetail;
import umc.kkijuk.server.detail.domain.CareerType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ActivityResponse implements BaseCareerResponse{
    private Long id;
    private CategoryResponse category;
    private String name;
    private String alias;
    private Boolean unknown;
    private String summary;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enddate;

    private String organizer;
    private String role;
    private int teamSize;
    private int contribution;
    private Boolean isTeam;
    private List<BaseCareerDetailResponse> detailList;

    public ActivityResponse(Activity activity) {
        this.id = activity.getId();
        this.category = new CategoryResponse(CareerType.ACTIVITY.getId(),CareerType.ACTIVITY.getDescription(),CareerType.ACTIVITY.name());
        this.name = activity.getName();
        this.alias = activity.getAlias();
        this.unknown = activity.getUnknown();
        this.summary = activity.getSummary();
        this.startdate = activity.getStartdate();
        this.enddate = activity.getEnddate();
        this.organizer = activity.getOrganizer();
        this.role  = activity.getRole();
        this.teamSize = activity.getTeamSize();
        this.contribution = activity.getContribution();
        this.isTeam = activity.getIsTeam();

    }
    public ActivityResponse(Activity activity, List<BaseCareerDetail> details) {
        this(activity);
        if (details != null && !details.isEmpty()) {
            this.detailList = details.stream()
                    .map(BaseCareerDetailResponse::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public LocalDate getEndDate() {
        return enddate;
    }

}
