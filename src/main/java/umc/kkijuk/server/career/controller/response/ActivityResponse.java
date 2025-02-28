package umc.kkijuk.server.career.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title="ActivityResponse : 내 커리어(대외활동) 생성 후 응답 DTO")
public class ActivityResponse implements BaseCareerResponse{
    @Schema(description = "대외활동 Id", example = "1")
    private Long id;
    @Schema(description = "대외활동 카테고리 정보", example = "{ \"categoryId\": 1, \"categoryKoName\": \"대외활동\", \"categoryEnName\": \"ACTIVITY\" }")
    private CategoryResponse category;
    @Schema(description = "대외활동 활동명", example = "00은행 홍보대사")
    private String name;
    @Schema(description = "대외활동 별칭", example = "00손해보험 대학생 서포터즈")
    private String alias;
    @Schema(description = "대외활동 기간 인지 여부", example = "false")
    private Boolean unknown;
    @Schema(description = "대외활동 활동 내역")
    private String summary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enddate;

    @Schema(description = "대외활동 주최", example = "00여행사")
    private String organizer;

    @Schema(description = "대외활동 역할", example = "팀장")
    private String role;
    @Schema(description = "대외활동 팀 규모", example = "2")
    private int teamSize;
    @Schema(description = "대외활동 기여도", example = "100")
    private int contribution;
    @Schema(description = "대외활동 팀 여부", example = "true")
    private Boolean isTeam;
    @Schema(description = "대외활동 활동 기록")
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

    @Override
    public LocalDate getStartDate() {
        return startdate;
    }

}
