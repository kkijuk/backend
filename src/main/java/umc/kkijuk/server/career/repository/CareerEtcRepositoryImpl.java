package umc.kkijuk.server.career.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.career.domain.CareerEtc;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CareerEtcRepositoryImpl implements CareerEtcRepository{
    private final CareerEtcJpaRepository careerEtcJpaRepository;
    @Override
    public List<CareerEtc> findByMemberId(Long memberId) {
        return careerEtcJpaRepository.findByMemberId(memberId);
    }

    @Override
    public List<CareerEtc> findByMemberIdAndNameContaining(Long id, String keyword) {
        return careerEtcJpaRepository.findByMemberIdAndNameContaining(id, keyword);
    }
    @Override
    public CareerEtc save(CareerEtc etc) {
        return careerEtcJpaRepository.save(etc);
    }
    @Override
    public Optional<CareerEtc> findById(Long etcId) {
        return careerEtcJpaRepository.findById(etcId);
    }
    @Override
    public void delete(CareerEtc etc) {
        careerEtcJpaRepository.delete(etc);
    }
}
