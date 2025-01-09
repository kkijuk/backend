package umc.kkijuk.server.career.controller.response;

import lombok.*;
import umc.kkijuk.server.career.domain.Project;
import umc.kkijuk.server.career.domain.ProjectType;
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
public class ProjectResponse implements BaseCareerResponse {
    private Long id;
    private CategoryResponse category;
    private String name;
    private String alias;
    private Boolean unknown;
    private String summary;
    private LocalDate startdate;
    private LocalDate enddate;
    private int teamSize;
    private Boolean isTeam;
    private int contribution;
    private ProjectType location;
    private List<BaseCareerDetailResponse> detailList;
    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.category = new CategoryResponse(CareerType.PROJECT.getId(),CareerType.PROJECT.getDescription(),CareerType.PROJECT.name());
        this.name = project.getName();
        this.alias = project.getAlias();
        this.unknown = project.getUnknown();
        this.summary = project.getSummary();
        this.startdate = project.getStartdate();
        this.enddate = project.getEnddate();
        this.teamSize = project.getTeamSize();
        this.isTeam = project.getIsTeam();
        this.contribution = project.getContribution();
        this.location = project.getLocation();

    }
    public ProjectResponse(Project project, List<BaseCareerDetail> details) {
        this(project);
        this.detailList = details.stream()
                .map(BaseCareerDetailResponse::new)
                .collect(Collectors.toList());
    }
    @Override
    public LocalDate getEndDate() {
        return enddate;
    }
}
