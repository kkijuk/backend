package umc.kkijuk.server.career.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import umc.kkijuk.server.career.domain.CareerEtc;
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
@Schema(title="EtcResponse : 내 커리어(기타) 생성 후 응답 DTO")
public class EtcResponse implements BaseCareerResponse{
    @Schema(description = "기타 Id", example = "1")
    private Long id;
    @Schema(description = "기타 카테고리 정보", example = "{ \"categoryId\": 7, \"categoryKoName\": \"기타\", \"categoryEnName\": \"ETC\" }")
    private CategoryResponse category;
    @Schema(description = "기타 활동명", example = "필리핀 해외봉사")
    private String name;
    @Schema(description = "기타 별칭", example = "000톤 정글 8")
    private String alias;
    @Schema(description = "기타 기간 인지 여부", example = "false")
    private Boolean unknown;
    @Schema(description = "기타 활동 내역")
    private String summary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enddate;

    @Schema(description = "대외활동 활동 기록")
    private List<BaseCareerDetailResponse> detailList;

    public EtcResponse(CareerEtc etc) {
        this.id = etc.getId();
        this.category = new CategoryResponse(CareerType.ETC.getId(),CareerType.ETC.getDescription(),CareerType.ETC.name());
        this.name = etc.getName();
        this.alias = etc.getAlias();
        this.unknown = etc.getUnknown();
        this.summary = etc.getSummary();
        this.startdate = etc.getStartdate();
        this.enddate = etc.getEnddate();
    }

    public EtcResponse(CareerEtc etc, List<BaseCareerDetail> details) {
        this(etc);
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
