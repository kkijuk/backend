package umc.kkijuk.server.recruit.controller.response;

import lombok.Builder;
import lombok.Getter;
import umc.kkijuk.server.recruit.domain.Recruit;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class RecruitListByEndDateResponse {
    private final int count;
    private final List<RecruitByEndDateInfoResponse> recruits;

    public static RecruitListByEndDateResponse from(Map<Recruit, String> recruits) {
        List<RecruitByEndDateInfoResponse> result = recruits.entrySet().stream()
                .map(entry -> RecruitByEndDateInfoResponse.from(entry.getKey(), entry.getValue()))
                .toList();

        return RecruitListByEndDateResponse.builder()
                .count(recruits.size())
                .recruits(result)
                .build();
    }
}
