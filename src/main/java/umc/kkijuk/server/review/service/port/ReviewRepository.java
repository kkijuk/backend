package umc.kkijuk.server.review.service.port;

import umc.kkijuk.server.recruit.domain.Recruit;
import umc.kkijuk.server.review.domain.RecruitReviewDto;
import umc.kkijuk.server.review.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Review save(Review review);

    Optional<Review> findById(Long id);

    Review getById(Long id);

    void delete(Review review);

    List<Review> findAllByRecruitId(Long id);

    List<RecruitReviewDto> findReviewByKeyword(Long memberId, String keyword);

    Optional<Review> findByRecruitAndTitle(Recruit recruit, String title);
}
