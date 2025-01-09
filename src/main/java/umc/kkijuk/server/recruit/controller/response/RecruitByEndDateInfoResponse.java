package umc.kkijuk.server.recruit.controller.response;

import lombok.Builder;
import lombok.Getter;
import umc.kkijuk.server.recruit.domain.Recruit;
import umc.kkijuk.server.recruit.domain.RecruitStatus;

import java.util.List;

@Getter
@Builder
public class RecruitByEndDateInfoResponse {
    private final Long recruitId;
    private final String title;
    private final String reviewTag; // 리뷰태그
    private final RecruitStatus status;
    private final List<String> tag;

    public static RecruitByEndDateInfoResponse from(Recruit recruit,String reviewTag) {
        return RecruitByEndDateInfoResponse.builder()
                .recruitId(recruit.getId())
                .reviewTag(reviewTag)
                .title(recruit.getTitle())
                .status(recruit.getStatus())
                .tag(recruit.getTags())
                .build();
    }
}
