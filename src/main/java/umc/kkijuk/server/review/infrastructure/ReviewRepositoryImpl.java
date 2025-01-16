package umc.kkijuk.server.review.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.recruit.domain.Recruit;
import umc.kkijuk.server.review.domain.RecruitReviewDto;
import umc.kkijuk.server.review.domain.Review;
import umc.kkijuk.server.review.service.port.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {
    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(ReviewEntity.from(review)).toModel();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewJpaRepository.findById(id).map(ReviewEntity::toModel);
    }

    @Override
    public Review getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("review", id));
    }

    @Override
    public void delete(Review review) {
        reviewJpaRepository.delete(ReviewEntity.from(review));
    }

    @Override
    public List<Review> findAllByRecruitId(Long id) {
        return reviewJpaRepository.findAllByRecruitId(id).stream().map(ReviewEntity::toModel).toList();
    }

    @Override
    public List<RecruitReviewDto> findReviewByKeyword(Long memberId, String keyword) {
        return reviewJpaRepository.findActiveRecruitReviewsByMemberIdAndKeyword(memberId, keyword)
                .stream().map(RecruitReviewDto::new).toList();
    }
    @Override
    public Optional<Review> findByRecruitAndTitle(Recruit recruit, String title) {
        return reviewJpaRepository
                .findByRecruitIdAndTitle(recruit.getId(), title)
                .map(ReviewEntity::toModel);
    }
}
