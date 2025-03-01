package umc.kkijuk.server.career.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import umc.kkijuk.server.career.domain.BaseCareer;
import umc.kkijuk.server.career.domain.EduCareer;
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
@Schema(title="EduCareerResponse : 내 커리어(교육) 생성 후 응답 DTO")
public class EduCareerResponse implements BaseCareerResponse {
    @Schema(description = "교육 Id", example = "1")
    private Long id;
    @Schema(description = "교육 카테고리 정보", example = "{ \"categoryId\": 3, \"categoryKoName\": \"교육\", \"categoryEnName\": \"EDU\" }")
    private CategoryResponse category;
    @Schema(description = "교육 활동명", example = "데이터 분석 세미나")
    private String name;
    @Schema(description = "교육 별칭", example = "000톤 정글 8기")
    private String alias;
    @Schema(description = "교육 기간 인지 여부", example = "false")
    private Boolean unknown;
    @Schema(description = "교육 활동 내역")
    private String summary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enddate;

    @Schema(description = "교육 주최", example = "대한상공회의소")
    private String organizer;

    @Schema(description = "교육 시간", example = "130")
    private int time;

    @Schema(description = "교육 활동 기록")
    private List<BaseCareerDetailResponse> detailList;
    public EduCareerResponse(EduCareer eduCareer) {
        this.id = eduCareer.getId();
        this.category = new CategoryResponse(CareerType.EDU.getId(),CareerType.EDU.getDescription(),CareerType.EDU.name());
        this.name = eduCareer.getName();
        this.alias = eduCareer.getAlias();
        this.unknown = eduCareer.getUnknown();
        this.summary = eduCareer.getSummary();
        this.startdate = eduCareer.getStartdate();
        this.enddate = eduCareer.getEnddate();
        this.organizer = eduCareer.getOrganizer();
        this.time = eduCareer.getTime();
    }

    public EduCareerResponse(EduCareer edu, List<BaseCareerDetail> details) {
        this(edu);
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
