package umc.kkijuk.server.recruit.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Comparator;
import lombok.Builder;
import lombok.Getter;
import umc.kkijuk.server.recruit.domain.Recruit;
import umc.kkijuk.server.review.domain.RecruitReviewDto;
import umc.kkijuk.server.review.controller.response.ReviewByKeyword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class RecruitReviewListByKeywordResponse {
    @Schema(description = "검색 키워드", example = "2024-07-19")
    private final String keyword;

    @Schema(description = "검색 키워드가 제목 또는 태그에 포함된 공고 목록")
    private final List<RecruitByKeyword> recruitResult;

    @Schema(description = "검색 키워드가 review에 포함된 공고 목록")
    private final List<ReviewByKeyword> reviewResult;

    public static RecruitReviewListByKeywordResponse from(String keyword, Map<Recruit, String> recruitMap, List<RecruitReviewDto> reviews) {
        List<ReviewByKeyword> reviewResult = new ArrayList<>();

        for (RecruitReviewDto recruitReviewDto : reviews) {
            ReviewByKeyword existingReview = reviewResult.stream()
                    .filter(r -> r.getRecruitId().equals(recruitReviewDto.getRecruitId()))
                    .findFirst()
                    .orElse(null);

            if (existingReview == null) reviewResult.add(ReviewByKeyword.from(recruitReviewDto));
            else existingReview.addReview(recruitReviewDto);
        }

        List<RecruitByKeyword> sortedRecruitList = recruitMap.entrySet().stream()
            .sorted(Comparator.comparing(entry -> entry.getKey().getEndTime(), Comparator.nullsLast(Comparator.reverseOrder())))
            .map(entry -> RecruitByKeyword.from(entry.getKey(), entry.getValue()))
            .toList();


        reviewResult.sort(Comparator.comparing(ReviewByKeyword::getEndTime, Comparator.nullsLast(Comparator.reverseOrder())));

        return RecruitReviewListByKeywordResponse.builder()
                .keyword(keyword)
                .recruitResult(sortedRecruitList)
                .reviewResult(reviewResult)
                .build();
    }
}
