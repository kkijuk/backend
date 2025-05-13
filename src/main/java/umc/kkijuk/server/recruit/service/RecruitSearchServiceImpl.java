package umc.kkijuk.server.recruit.service;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.recruit.controller.port.RecruitSearchService;
import umc.kkijuk.server.recruit.controller.response.RecruitReviewListByKeywordResponse;
import umc.kkijuk.server.recruit.domain.Recruit;
import umc.kkijuk.server.review.domain.RecruitReviewDto;
import umc.kkijuk.server.recruit.service.port.RecruitRepository;
import umc.kkijuk.server.review.domain.Review;
import umc.kkijuk.server.review.service.port.ReviewRepository;

import java.lang.management.ManagementPermission;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruitSearchServiceImpl implements RecruitSearchService {
    private final RecruitRepository recruitRepository;
    private final ReviewRepository reviewRepository;

    public RecruitReviewListByKeywordResponse findRecruitByKeyword(Member requestMember, String keyword) {
        String trimKeyword = Normalizer.normalize(keyword.trim(), Form.NFC);
        List<Recruit> recruits = recruitRepository.searchRecruitByKeyword(requestMember.getId(), trimKeyword);
        List<RecruitReviewDto> reviews = reviewRepository.findReviewByKeyword(requestMember.getId(), trimKeyword);

        Map<Recruit, String> reviewMap = new HashMap<>();
        for (Recruit recruit : recruits) {
            String reviewTitle = reviewRepository.findAllByRecruitId(recruit.getId())
                    .stream().max(Comparator.comparing(Review::getDate))
                    .map(Review::getTitle).orElse("");
            reviewMap.put(recruit, reviewTitle);
        }

        for (RecruitReviewDto review : reviews) {
            log.info("keyword: {}, result: {}", trimKeyword, review.getReviewContent());
        }


        return RecruitReviewListByKeywordResponse.from(keyword, reviewMap, reviews);
    }
}
