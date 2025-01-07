package umc.kkijuk.server.recruit.controller.response;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import umc.kkijuk.server.recruit.domain.Recruit;

import java.util.List;

@Getter
@Builder
public class RecruitListByEndTimeAfterResponse {
    private final int totalCount;
    private final List<RecruitByEndDate> outputs;

    public static RecruitListByEndTimeAfterResponse from(Map<Recruit, String> recruits) {
        return RecruitListByEndTimeAfterResponse.builder()
                .totalCount(recruits.size())
                .outputs(RecruitByEndDate.from(recruits))
                .build();
    }
}
