package umc.kkijuk.server.introduce.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc.kkijuk.server.introduce.domain.Introduce;
import umc.kkijuk.server.introduce.domain.MasterIntroduce;
import umc.kkijuk.server.introduce.dto.QuestionDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class MasterIntroduceResponse {
    private Long id;
    private Long memberId;
    private String oneLiner;
    private List<QuestionDto> questionList;
    private String updatedAt;
    private int state;

    @Builder
    public MasterIntroduceResponse(MasterIntroduce masterIntroduce, List<QuestionDto> questionList) {
        this.id = masterIntroduce.getId();
        this.oneLiner = masterIntroduce.getOneLiner();
        this.memberId=masterIntroduce.getMemberId();
        this.questionList = questionList;
        this.updatedAt = formatUpdatedAt(masterIntroduce.getUpdatedAt());
        this.state=masterIntroduce.getState();
    }

    private String formatUpdatedAt(LocalDateTime updatedAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return updatedAt != null ? updatedAt.format(formatter) : null;
    }

}
