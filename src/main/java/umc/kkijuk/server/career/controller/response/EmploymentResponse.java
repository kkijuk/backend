package umc.kkijuk.server.career.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import umc.kkijuk.server.career.domain.Employment;
import umc.kkijuk.server.career.domain.JobType;
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
public class EmploymentResponse implements BaseCareerResponse{
    @Schema(description = "경력 Id", example = "1")
    private Long id;
    @Schema(description = "경력 카테고리 정보", example = "{ \"categoryId\": 3, \"categoryKoName\": \"교육\", \"categoryEnName\": \"EDU\" }")
    private CategoryResponse category;
    @Schema(description = "경력 활동명", example = "학원 채점 아르바이트")
    private String name;
    @Schema(description = "경력 근무처", example = "근무처")
    private String alias;
    @Schema(description = "경력 기간 인지 여부", example = "false")
    private Boolean unknown;
    @Schema(description = "경력 활동 내역")
    private String summary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enddate;


    @Schema(description = "직무 유형(분류)", example = "FULL_TIME", type = "string", allowableValues = {"FULL_TIME", "PART_TIME", "INTERNSHIP", "FREELANCE"})
    private JobType type;
    @Schema(description = "경력 직급/직위", example = "인턴,보조강사")

    private String position;
    @Schema(description = "경력 직무/분야", example = "서비스업,iOS 개발 등")
    private String field;
    @Schema(description = "경력 활동 기록")
    private List<BaseCareerDetailResponse> detailList;
    @Override
    public LocalDate getEndDate() {
        return enddate;
    }
    public EmploymentResponse(Employment employment) {
        this.id = employment.getId();
        this.category = new CategoryResponse(CareerType.EMP.getId(),CareerType.EMP.getDescription(),CareerType.EMP.name());
        this.name = employment.getName();
        this.alias = employment.getAlias();
        this.unknown = employment.getUnknown();
        this.summary = employment.getSummary();
        this.startdate = employment.getStartdate();
        this.enddate = employment.getEnddate();
        this.type = employment.getType();
        this.position = employment.getPosition();
        this.field = employment.getField();

    }

    public EmploymentResponse(Employment emp, List<BaseCareerDetail> details) {
        this(emp);
        this.detailList = details.stream()
                .map(BaseCareerDetailResponse::new)
                .collect(Collectors.toList());
    }
    @Override
    public LocalDate getStartDate() {
        return startdate;
    }

}
