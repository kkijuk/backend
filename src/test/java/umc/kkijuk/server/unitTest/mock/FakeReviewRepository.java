package umc.kkijuk.server.unitTest.mock;

import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.recruit.domain.Recruit;
import umc.kkijuk.server.review.domain.RecruitReviewDto;
import umc.kkijuk.server.review.domain.Review;
import umc.kkijuk.server.review.service.port.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeReviewRepository implements ReviewRepository {
    private final AtomicLong authGeneratedId = new AtomicLong(0);
    private final List<Review> data = new ArrayList<>();

    @Override
    public Review save(Review review) {
        if (review.getId() == null || review.getId() == 0) {
            Review newReview = Review.builder()
                    .id(authGeneratedId.incrementAndGet())
                    .memberId(review.getMemberId())
                    .recruitId(review.getRecruitId())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .date(review.getDate())
                    .build();
            data.add(newReview);
            return newReview;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), review.getId()));
            data.add(review);
            return review;
        }
    }

    @Override
    public Optional<Review> findById(Long id) {
        return data.stream()
                .filter(item -> item.getId().equals(id))
                .findAny();
    }

    @Override
    public Review getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("review", id));
    }

    @Override
    public void delete(Review review) {
        data.remove(review);
    }

    @Override
    public List<Review> findAllByRecruitId(Long id) {
        return data.stream()
                .filter(review -> review.getRecruitId().equals(id))
                .toList();
    }

    @Override
    public List<RecruitReviewDto> findReviewByKeyword(Long memberId, String keyword) {
        return List.of();
    }

    @Override
    public Optional<Review> findByRecruitAndTitle(Recruit recruit, String title) {
        return data.stream()
                .filter(review -> review.getRecruitId().equals(recruit.getId()) && review.getTitle().equals(title))
                .findAny();
    }
}
