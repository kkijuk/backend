package umc.kkijuk.server.review.infrastructure;

import jakarta.persistence.*;
import umc.kkijuk.server.review.domain.Review;

import java.time.LocalDate;

@Entity
@Table(name = "review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long recruitId;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String content;

    @Column(nullable = false)
    private LocalDate date;


    public static ReviewEntity from(Review review) {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.id = review.getId();
        reviewEntity.memberId = review.getMemberId();
        reviewEntity.recruitId = review.getRecruitId();
        reviewEntity.title = review.getTitle();
        reviewEntity.content = review.getContent();
        reviewEntity.date = review.getDate();
        return reviewEntity;
    }

    public Review toModel() {
        return Review.builder()
                .id(id)
                .memberId(memberId)
                .recruitId(recruitId)
                .title(title)
                .content(content)
                .date(date)
                .build();
    }
}
