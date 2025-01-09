package umc.kkijuk.server.career.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.career.domain.Competition;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CompetitionRepositoryImpl implements CompetitionRepository{
    private final CompetitionJpaRepository competitionJpaRepository;
    @Override
    public List<Competition> findByMemberId(Long memberId) {
        return competitionJpaRepository.findByMemberId(memberId);
    }
    @Override
    public List<Competition> findByMemberIdAndNameContaining(Long memberId, String keyword) {
        return competitionJpaRepository.findByMemberIdAndNameContaining(memberId,keyword);
    }

    @Override
    public Competition save(Competition competition) {
        return competitionJpaRepository.save(competition);
    }

    @Override
    public Optional<Competition> findById(Long competitionId) {
        return competitionJpaRepository.findById(competitionId);
    }

    @Override
    public void delete(Competition comp) {
        competitionJpaRepository.delete(comp);
    }
}
