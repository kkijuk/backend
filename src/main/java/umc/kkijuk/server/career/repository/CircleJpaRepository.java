package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.kkijuk.server.career.domain.Circle;

import java.util.List;

public interface CircleJpaRepository extends JpaRepository<Circle, Long> {
    List<Circle> findByMemberId(Long memberId);
    @Query("SELECT circle FROM Circle circle WHERE circle.memberId = :memberId "
        + "AND (circle.name LIKE %:keyword% "
        + "OR circle.alias LIKE %:keyword%)")
    List<Circle> findByMemberIdAndNameContaining(Long memberId, String keyword);
}
