package umc.kkijuk.server.career.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import umc.kkijuk.server.career.domain.Circle;
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
@Schema(title="CircleResponse : 내 커리어(동아리) 생성 후 응답 DTO")
public class CircleResponse implements BaseCareerResponse{
    @Schema(description = "동아리 Id", example = "1")
    private Long id;
    @Schema(description = "동아리 카테고리 정보", example = "{ \"categoryId\": 5, \"categoryKoName\": \"동아리\", \"categoryEnName\": \"CIRCLE\" }")
    private CategoryResponse category;
    @Schema(description = "동아리 활동명", example = "광고 기획 연합동아리")
    private String name;
    @Schema(description = "동아리 별칭", example = "UMC")
    private String alias;
    @Schema(description = "동아리 기간 인지 여부", example = "false")
    private Boolean unknown;
    @Schema(description = "동아리 활동 내역")
    private String summary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enddate;

    @Schema(description = "동아리 소속", example = "true")
    private Boolean location;
    @Schema(description = "동아리 역할", example = "동아리장")
    private String role;

    @Schema(description = "동아리 활동 기록")
    private List<BaseCareerDetailResponse> detailList;
    public CircleResponse(Circle circle) {
        this.id = circle.getId();
        this.category = new CategoryResponse(CareerType.CIRCLE.getId(),CareerType.CIRCLE.getDescription(),CareerType.CIRCLE.name());
        this.name = circle.getName();
        this.alias = circle.getAlias();
        this.unknown = circle.getUnknown();
        this.summary = circle.getSummary();
        this.startdate = circle.getStartdate();
        this.enddate = circle.getEnddate();
        this.location = circle.getLocation();
        this.role = circle.getRole();
    }
    public CircleResponse(Circle circle, List<BaseCareerDetail> details) {
        this(circle);
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
