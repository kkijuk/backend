package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.kkijuk.server.career.domain.Competition;

import java.util.List;

public interface CompetitionJpaRepository extends JpaRepository<Competition, Long> {
    List<Competition> findByMemberId(Long memberId);
    @Query("SELECT c FROM Competition c WHERE c.memberId = :memberId "
        + "AND (c.name LIKE %:keyword% "
        + "OR c.alias LIKE %:keyword%)")
    List<Competition> findByMemberIdAndNameContaining(Long memberId, String keyword);
}
