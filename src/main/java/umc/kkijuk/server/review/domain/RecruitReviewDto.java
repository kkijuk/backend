package umc.kkijuk.server.review.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.kkijuk.server.recruit.domain.RecruitStatus;
import umc.kkijuk.server.review.infrastructure.RecruitReviewDtoInterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RecruitReviewDto {
    private final Long memberId;
    private final Long recruitId;
    private final String recruitTitle;
    private final List<String> tags;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final RecruitStatus status;
    private final Long reviewId;
    private final String reviewTitle;
    private final String reviewContent;
    private final LocalDate reviewDate;

    public RecruitReviewDto(RecruitReviewDtoInterface entity) {
        this.memberId = entity.getMemberId();
        this.recruitId = entity.getRecruitId();
        this.recruitTitle = entity.getRecruitTitle();
        this.tags = entity.getTags();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.status = entity.getStatus();


        this.reviewId = entity.getReviewId();
        this.reviewTitle = entity.getReviewTitle();
        this.reviewContent = entity.getReviewContent();
        this.reviewDate = entity.getReviewDate();
    }
}