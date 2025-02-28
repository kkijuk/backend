package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.career.domain.Competition;

import java.time.LocalDate;
import java.util.List;

public interface CompetitionJpaRepository extends JpaRepository<Competition, Long> {
    List<Competition> findByMemberId(Long memberId);
    @Query("SELECT c FROM Competition c WHERE c.memberId = :memberId "
        + "AND (c.name LIKE %:keyword% "
        + "OR c.alias LIKE %:keyword%)")
    List<Competition> findByMemberIdAndNameContaining(Long memberId, String keyword);

    @Modifying(clearAutomatically=true)
    @Query("UPDATE Competition a SET a.enddate = :today WHERE a.unknown = true")
    void updateUnknownEndDates(LocalDate today);
}
