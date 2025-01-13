package umc.kkijuk.server.review.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findAllByRecruitId(Long id);

    @Query(value = "SELECT r.member_id, r.id as recruit_id, r.title as recruit_title, r.tags, r.start_time, r.end_time, r.status, rv.id as review_id, rv.title as review_title, rv.content as review_content, rv.date as review_date " +
            "FROM recruit as r " +
            "JOIN review as rv ON rv.recruit_id = r.id " +
            "WHERE r.member_id = :memberId " +
            "AND r.active = true " +
            "AND rv.content LIKE %:keyword% " +
            "ORDER BY rv.date DESC, r.end_time DESC", nativeQuery = true)
    List<RecruitReviewDtoInterface> findActiveRecruitReviewsByMemberIdAndKeyword(@Param("memberId") Long memberId,
                                                                                 @Param("keyword") String keyword);

    @Query("SELECT rv FROM ReviewEntity rv WHERE rv.recruitId = :recruitId AND rv.title = :title")
    Optional<ReviewEntity> findByRecruitIdAndTitle(@Param("recruitId") Long recruitId, @Param("title") String title);
}
