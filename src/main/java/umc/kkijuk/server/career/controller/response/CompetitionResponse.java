package umc.kkijuk.server.career.controller.response;

import lombok.*;
import umc.kkijuk.server.career.domain.Competition;
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
public class CompetitionResponse implements BaseCareerResponse {
    private Long id;
    private CategoryResponse category;
    private String name;
    private String alias;
    private Boolean unknown;
    private String summary;
    private LocalDate startdate;
    private LocalDate enddate;
    private String organizer;
    private int teamSize;
    private int contribution;
    private Boolean isTeam;
    private List<BaseCareerDetailResponse> detailList;
    public CompetitionResponse(Competition competition) {
        this.id = competition.getId();
        this.category = new CategoryResponse(CareerType.COM.getId(),CareerType.COM.getDescription(),CareerType.COM.name());
        this.name = competition.getName();
        this.alias = competition.getAlias();
        this.unknown = competition.getUnknown();
        this.summary = competition.getSummary();
        this.startdate = competition.getStartdate();
        this.enddate = competition.getEnddate();
        this.organizer = competition.getOrganizer();
        this.teamSize = competition.getTeamSize();
        this.contribution = competition.getContribution();
        this.isTeam = competition.getIsTeam();

    }

    public CompetitionResponse(Competition competition, List<BaseCareerDetail> details) {
        this(competition);
        this.detailList = details.stream()
                .map(BaseCareerDetailResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public LocalDate getEndDate() {
        return enddate;
    }
}
