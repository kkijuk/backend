package umc.kkijuk.server.career.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title="ProjectResponse : 내 커리어(프로젝트) 생성 후 응답 DTO")
public class ProjectResponse implements BaseCareerResponse {
    @Schema(description = "프로젝트 Id", example = "1")
    private Long id;
    @Schema(description = "프로젝트 카테고리 정보", example = "{ \"categoryId\": 2, \"categoryKoName\": \"프로젝트\", \"categoryEnName\": \"PROJECT\" }")
    private CategoryResponse category;
    @Schema(description = "프로젝트 활동명", example = "광고 기획 동아리")
    private String name;
    @Schema(description = "프로젝트 별칭", example = "UMC")
    private String alias;
    @Schema(description = "프로젝트 기간 인지 여부", example = "false")
    private Boolean unknown;
    @Schema(description = "프로젝트 활동 내역")
    private String summary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enddate;

    @Schema(description = "프로젝트 팀 규모", example = "2")
    private int teamSize;
    @Schema(description = "프로젝트 팀 여부", example = "true")
    private Boolean isTeam;
    @Schema(description = "프로젝트 기여도", example = "100")
    private int contribution;

    @Schema(description = "프로젝트 소속", example = "OTHER", type = "string", allowableValues = {"ON_CAMPUS", "OFF_CAMPUS", "OTHER"})
    private ProjectType location;

    @Schema(description = "프로젝트 활동 기록")
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
    @Override
    public LocalDate getStartDate() {
        return startdate;
    }
}
