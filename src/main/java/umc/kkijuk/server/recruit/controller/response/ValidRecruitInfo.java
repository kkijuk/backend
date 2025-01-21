package umc.kkijuk.server.recruit.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import umc.kkijuk.server.recruit.domain.ValidRecruitDto;

import java.util.List;

@Getter
@Builder
public class ValidRecruitInfo {
    private Long id;
    private String title;
    private List<String> tags;
    private String link;
    private String reviewTag;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endTime;

    public static ValidRecruitInfo from(ValidRecruitDto dto) {
        return ValidRecruitInfo.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .link(dto.getLink())
                .endTime(dto.getEndTime())
                .reviewTag(dto.getReviewTag())
                .tags(dto.getTags())
                .build();
    }
}
