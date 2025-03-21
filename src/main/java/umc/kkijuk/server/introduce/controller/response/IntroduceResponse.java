package umc.kkijuk.server.introduce.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc.kkijuk.server.introduce.domain.Introduce;
import umc.kkijuk.server.introduce.dto.QuestionDto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
public class IntroduceResponse {
    private Long id;
    private Long recruitId;
    private Long memberId;
    private String recruitTitle;
    private List<QuestionDto> questionList;
    private String deadline;
    private List<String> tags;
    private String link;
    private String updatedAt;
    private String timeSinceUpdate;
    /*   private List<String> introduceList;*/
    private int state;
    private Boolean recruitEndState;

    @Builder
    public IntroduceResponse(Introduce introduce, List<QuestionDto> questionList) {
        this.id = introduce.getId();
        this.recruitId=introduce.getRecruit().toModel().getId();
        this.memberId=introduce.getMemberId();
        this.recruitTitle=introduce.getRecruit().toModel().getTitle();
        this.questionList = questionList;
        this.deadline=formatUpdatedAt(introduce.getRecruit().toModel().getEndTime());
        this.tags=introduce.getRecruit().toModel().getTags();
        this.link=introduce.getRecruit().toModel().getLink();
        this.updatedAt = formatUpdatedAt(introduce.getUpdatedAt());
        this.timeSinceUpdate = calculateTimeUntilDeadline(introduce.getRecruit().toModel().getEndTime());
        this.state=introduce.getState();
        this.recruitEndState = isRecruitEnded(introduce.getRecruit().toModel().getEndTime());
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

    private boolean isRecruitEnded(LocalDateTime deadline) {
        return deadline.isBefore(LocalDateTime.now());
    }
}
