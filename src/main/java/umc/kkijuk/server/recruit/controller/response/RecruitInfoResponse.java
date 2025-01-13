package umc.kkijuk.server.recruit.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import umc.kkijuk.server.introduce.domain.Introduce;
import umc.kkijuk.server.recruit.domain.Recruit;
import umc.kkijuk.server.recruit.domain.RecruitStatus;
import umc.kkijuk.server.review.domain.Review;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
public class RecruitInfoResponse {
    private final String title;

    @Schema(description = "공고 모집 시작 날짜", example = "2022-09-18 10:11", pattern = "yyyy-MM-dd HH:mm", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private final LocalDateTime startTime;

    @Schema(description = "공고 모집 마감 날짜", example = "2022-09-25 10:11", pattern = "yyyy-MM-dd HH:mm", type = "string" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private final LocalDateTime endTime;

    @Schema(description = "공고 지원 날짜", example = "2024-07-19")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate applyDate;

    private final RecruitStatus status;
    private final List<String> tags;
    private final int reviewCount;
    private final String link;
    private final List<ReviewResponse> reviews;
    private final int introduceState;

    public static RecruitInfoResponse from(Recruit recruit, List<Review> reviews, int introduceState) {
        return RecruitInfoResponse.builder()
                .title(recruit.getTitle())
                .startTime(recruit.getStartTime())
                .endTime(recruit.getEndTime())
                .status(recruit.getStatus())
                .applyDate(recruit.getApplyDate())
                .tags(recruit.getTags())
                .link(recruit.getLink())
                .reviewCount(reviews.size())
                .reviews(reviews.stream().map(ReviewResponse::from).toList())
                .introduceState(introduceState)
                .build();
    }
}
