package umc.kkijuk.server.introduce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class IntroduceReqDto {
    private String oneLiner;
    private List<QuestionDto> questionList;
    private int state;
}
