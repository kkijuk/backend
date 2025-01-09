package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.kkijuk.server.career.domain.Competition;

import java.util.List;

public interface CompetitionJpaRepository extends JpaRepository<Competition, Long> {
    List<Competition> findByMemberId(Long memberId);
    List<Competition> findByMemberIdAndNameContaining(Long memberId, String keyword);
}
