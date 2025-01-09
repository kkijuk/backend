package umc.kkijuk.server.recruit.controller.response;

import lombok.Builder;
import lombok.Getter;
import umc.kkijuk.server.recruit.domain.Recruit;

import java.time.LocalDate;
import java.util.*;

@Getter
@Builder
public class RecruitByEndDate {
    private final LocalDate endDate;
    private int count;
    private List<RecruitByEndDateInfoResponse> recruits;

    public static List<RecruitByEndDate> from(Map<Recruit, String> recruits) {
        List<RecruitByEndDate> outputs = new ArrayList<>();
        recruits.forEach((recruit,reviewTag) -> {
            Optional<RecruitByEndDate> o = outputs.stream()
                    .filter(item -> recruit.getEndTime().toLocalDate().isEqual(item.getEndDate()))
                    .findFirst();
            o.ifPresent(recruitByEndDate -> {
                recruitByEndDate.recruits.add(RecruitByEndDateInfoResponse.from(recruit, reviewTag));
                recruitByEndDate.count++;
            });
            if (o.isEmpty()) {
                outputs.add(RecruitByEndDate.builder()
                        .endDate(recruit.getEndTime().toLocalDate())
                        .count(1)
                        .recruits(new ArrayList<>(Arrays.asList(RecruitByEndDateInfoResponse.from(recruit, reviewTag))))
                        .build());
            }
        });

        outputs.sort(Comparator.comparing(RecruitByEndDate::getEndDate));
        return outputs;
    }
}
