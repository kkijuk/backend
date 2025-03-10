package umc.kkijuk.server.unitTest.review.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.common.domian.exception.ReviewRecruitMismatchException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.recruit.domain.Recruit;
import umc.kkijuk.server.review.controller.port.ReviewService;
import umc.kkijuk.server.review.domain.Review;
import umc.kkijuk.server.review.domain.ReviewCreate;
import umc.kkijuk.server.review.domain.ReviewUpdate;
import umc.kkijuk.server.review.service.ReviewServiceImpl;
import umc.kkijuk.server.unitTest.mock.FakeReviewRepository;
import umc.kkijuk.server.review.service.port.ReviewRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    private ReviewService reviewService;
    private Member requestMember;
    private final Long testMemberId = 3333L;
    private Recruit recruit;
    private final Long testRecruitId = 4444L;

    @BeforeEach
    void Init() {
        requestMember = Member.builder()
                .id(testMemberId)
                .email("test-email@test.com")
                .name("test-name")
                .phoneNumber("test-test-test")
                .birthDate(LocalDate.of(2024, 7, 25))
//                .password("test-password")
                .userState(State.ACTIVATE)
                .build();

        recruit = Recruit.builder()
                .id(testRecruitId)
                .memberId(testMemberId)
                .build();

        ReviewRepository reviewRepository = new FakeReviewRepository();
        reviewService = ReviewServiceImpl.builder()
                .reviewRepository(reviewRepository)
                .build();

        Review review = Review.builder()
                .recruitId(testRecruitId)
                .title("test-title")
                .content("test-content")
                .date(LocalDate.of(2024, 7, 21))
                .build();

        reviewRepository.save(review);
    }

    @Test
    void crate_새로운_review_만들기() {
        //given
        ReviewCreate reviewCreate = ReviewCreate.builder()
                .title("new-title")
                .content("new-content")
                .date(LocalDate.of(2024, 7, 21))
                .build();

        //when
        Review result = reviewService.create(requestMember, recruit, reviewCreate);

        //then
        assertAll(
            () -> assertThat(result.getRecruitId()).isEqualTo(recruit.getId()),
            () -> assertThat(result.getTitle()).isEqualTo(reviewCreate.getTitle()),
            () -> assertThat(result.getContent()).isEqualTo(reviewCreate.getContent()),
            () -> assertThat(result.getDate()).isEqualTo(LocalDate.of(2024, 7, 21))
        );
    }

    @Test
    void create_새로운_review_만들기_nullable() {
        //given
        ReviewCreate reviewCreate = ReviewCreate.builder()
                .title("new-title")
                .date(LocalDate.of(2024, 7, 21))
                .build();

        //when
        Review result = reviewService.create(requestMember, recruit, reviewCreate);

        //then
        assertAll(
                () -> assertThat(result.getRecruitId()).isEqualTo(recruit.getId()),
                () -> assertThat(result.getTitle()).isEqualTo(reviewCreate.getTitle()),
                () -> assertThat(result.getContent()).isNull(),
                () -> assertThat(result.getDate()).isEqualTo(LocalDate.of(2024, 7, 21))
        );
    }

    @Test
    void update_존재하던_review_수정() {
        //given
        Long reviewId = 1L;
        ReviewUpdate reviewUpdate = ReviewUpdate.builder()
                .title("changed-title")
                .content("changed-content")
                .date(LocalDate.of(2024, 7, 30))
                .build();

        //when
        Review result = reviewService.update(requestMember, recruit, reviewId, reviewUpdate);

        //then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getRecruitId()).isEqualTo(testRecruitId),
                () -> assertThat(result.getTitle()).isEqualTo("changed-title"),
                () -> assertThat(result.getContent()).isEqualTo("changed-content"),
                () -> assertThat(result.getDate()).isEqualTo(LocalDate.of(2024, 7, 30))
        );
    }

    @Test
    void update_존재하던_review_수정_content_이미존재했지만_null로변경() {
        //given
        Long reviewId = 1L;
        ReviewUpdate reviewUpdate = ReviewUpdate.builder()
                .title("changed-title")
                .date(LocalDate.of(2024, 7, 30))
                .build();

        //when
        Review result = reviewService.update(requestMember, recruit, reviewId, reviewUpdate);

        //then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getRecruitId()).isEqualTo(testRecruitId),
                () -> assertThat(result.getTitle()).isEqualTo("changed-title"),
                () -> assertThat(result.getContent()).isNull(),
                () -> assertThat(result.getDate()).isEqualTo(LocalDate.of(2024, 7, 30))
        );
    }

    @Test
    void delete_존재하던_review_제거() {
        //given
        ReviewCreate reviewCreate = ReviewCreate.builder()
                .title("new-title")
                .content("new-content")
                .date(LocalDate.of(2024, 7, 21))
                .build();

        Review review = reviewService.create(requestMember, recruit, reviewCreate);

        //when
        reviewService.delete(requestMember, recruit, review.getId());

        //then
        assertThatThrownBy(
            () -> reviewService.getById(review.getId())).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_존재하지않은_review에대한_제거요청() {
        //given
        //when
        //then
        assertThatThrownBy(
                () -> reviewService.delete(requestMember, recruit, testMemberId)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_review의_공고ID가_요청한_공고ID와_다른경우() {
        //given
        ReviewCreate reviewCreate = ReviewCreate.builder()
                .title("new-title")
                .content("new-content")
                .date(LocalDate.of(2024, 7, 21))
                .build();
        Recruit anotherRecruit = Recruit.builder().id(5555L).memberId(testMemberId).build();
        Review review = reviewService.create(requestMember, recruit, reviewCreate);

        //when
        //then
        assertThatThrownBy(
                () -> reviewService.delete(requestMember, anotherRecruit, review.getId())).isInstanceOf(ReviewRecruitMismatchException.class);
    }

//    @Test
//    void findAllByRecruitId_공고의_모든_review찾기() {
//        //given
//        ReviewCreate reviewCreate = ReviewCreate.builder()
//                .title("new-title")
//                .content("new-content")
//                .date(LocalDate.of(2024, 7, 21))
//                .build();
//
//        for (int i = 0; i < 10; i++)
//            reviewService.create(requestMember, recruit, reviewCreate);
//
//        //when
//        List<Review> reviews = reviewService.findAllByRecruit(requestMember, recruit);
//
//        //then
//        assertThat(reviews.size()).isEqualTo(11);
//    }
}