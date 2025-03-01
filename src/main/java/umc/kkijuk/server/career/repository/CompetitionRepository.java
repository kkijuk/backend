package umc.kkijuk.server.career.repository;

import umc.kkijuk.server.career.domain.Competition;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompetitionRepository {
    List<Competition> findByMemberId(Long memberId);
    List<Competition> findByMemberIdAndNameContaining(Long memberId, String keyword);

    Competition save(Competition competition);

    Optional<Competition> findById(Long competitionId);

    void delete(Competition comp);
    void updateUnknownEndDates(LocalDate today);

}
