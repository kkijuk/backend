package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.career.domain.Circle;

import java.time.LocalDate;
import java.util.List;

public interface CircleJpaRepository extends JpaRepository<Circle, Long> {
    List<Circle> findByMemberId(Long memberId);
    @Query("SELECT circle FROM Circle circle WHERE circle.memberId = :memberId "
        + "AND (circle.name LIKE %:keyword% "
        + "OR circle.alias LIKE %:keyword%)")
    List<Circle> findByMemberIdAndNameContaining(Long memberId, String keyword);

    @Modifying(clearAutomatically=true)
    @Query("UPDATE Circle a SET a.enddate = :today WHERE a.unknown = true")
    void updateUnknownEndDates(LocalDate today);
}
