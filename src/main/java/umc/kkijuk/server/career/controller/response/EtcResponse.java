package umc.kkijuk.server.career.controller.response;

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
public class EtcResponse implements BaseCareerResponse{
    private Long id;
    private CategoryResponse category;
    private String name;
    private String alias;
    private Boolean unknown;
    private String summary;
    private LocalDate startdate;
    private LocalDate enddate;
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
}
