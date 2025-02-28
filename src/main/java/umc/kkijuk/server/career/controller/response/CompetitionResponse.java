package umc.kkijuk.server.career.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title="CompetitionResponse : 내 커리어(공모전대회) 생성 후 응답 DTO")
public class CompetitionResponse implements BaseCareerResponse {
    @Schema(description = "공모전대회 Id", example = "1")
    private Long id;
    @Schema(description = "공모전대회 카테고리 정보", example = "{ \"categoryId\": 6, \"categoryKoName\": \"공모전대회\", \"categoryEnName\": \"COM\" }")
    private CategoryResponse category;
    @Schema(description = "공모전대회 활동명", example = "PR 아이디어 공모전")
    private String name;
    @Schema(description = "공모전대회 별칭", example = "2025 로레0 브랜드스톰 공모전")
    private String alias;
    @Schema(description = "공모전대회 기간 인지 여부", example = "true")
    private Boolean unknown;
    @Schema(description = "공모전대회 활동 내역")
    private String summary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enddate;

    @Schema(description = "공모전대회 주최", example = "00문화재단")
    private String organizer;
    @Schema(description = "공모전대회 팀 규모", example = "2")
    private int teamSize;
    @Schema(description = "공모전대회 기여도", example = "100")
    private int contribution;
    @Schema(description = "공모전대회 팀 여부", example = "true")
    private Boolean isTeam;
    @Schema(description = "공모전대회 활동 기록")
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
    @Override
    public LocalDate getStartDate() {
        return startdate;
    }
}
