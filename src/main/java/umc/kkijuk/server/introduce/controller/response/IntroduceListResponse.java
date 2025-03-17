package umc.kkijuk.server.introduce.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc.kkijuk.server.introduce.domain.Introduce;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class IntroduceListResponse {
    private Long id;
    private Long memberId;
    private Long recruitId;
    private String recruitTitle;
    private String deadline;
    private String updatedAt;
    private String timeSinceUpdate;
    private int state;

    @Builder
    public IntroduceListResponse(Introduce introduce) {
        this.id = introduce.getId();
        this.memberId = introduce.getMemberId();
        this.recruitId=introduce.getRecruit().toModel().getId();
        this.recruitTitle=introduce.getRecruit().toModel().getTitle();
        this.deadline=formatUpdatedAt(introduce.getRecruit().toModel().getEndTime());
        this.updatedAt = formatUpdatedAt(introduce.getUpdatedAt());
        this.timeSinceUpdate = calculateTimeUntilDeadline(introduce.getRecruit().toModel().getEndTime());
        this.state=introduce.getState();
    }

    private String formatUpdatedAt(LocalDateTime updatedAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return updatedAt != null ? updatedAt.format(formatter) : null;
    }

    private String calculateTimeUntilDeadline(LocalDateTime deadline) {
        LocalDate today = LocalDate.now();
        LocalDate deadlineDate = deadline.toLocalDate();

        long days = ChronoUnit.DAYS.between(today, deadlineDate);

        if (days < 0) {
            return "D+" + Math.abs(days);
        }

        return "D-" + days;
    }
}
