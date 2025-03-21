package umc.kkijuk.server.dashboard.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc.kkijuk.server.introduce.domain.Introduce;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
public class IntroduceRemindResponse {
    private Long introduceId;
    private String recruitTitle;
    private String deadline;
    private Boolean recruitEndState;

    @Builder
    public IntroduceRemindResponse(Introduce introduce) {
        this.introduceId = introduce.getId();
        this.recruitTitle = introduce.getRecruit().toModel().getTitle();
        this.deadline = calculateTimeUntilDeadline(LocalDateTime.now(), introduce.getRecruit().toModel().getEndTime());
        this.recruitEndState = isRecruitEnded(introduce.getRecruit().toModel().getEndTime());
    }

    private String calculateTimeUntilDeadline(LocalDateTime updatedAt, LocalDateTime deadline) {
        Duration duration = Duration.between(updatedAt, deadline);
        long days = duration.toDays() + 1;
        return "D-" + days;
    }

    private boolean isRecruitEnded(LocalDateTime deadline) {
        return deadline.isBefore(LocalDateTime.now());
    }
}
