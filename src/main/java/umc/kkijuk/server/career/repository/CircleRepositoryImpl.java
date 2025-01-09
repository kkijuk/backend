package umc.kkijuk.server.career.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.career.domain.Circle;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CircleRepositoryImpl implements CircleRepository{
    private final CircleJpaRepository circleJpaRepository;
    @Override
    public List<Circle> findByMemberId(Long memberId) {
        return circleJpaRepository.findByMemberId(memberId);
    }

    @Override
    public List<Circle> findByMemberIdAndNameContaining(Long memberId, String keyword) {
        return circleJpaRepository.findByMemberIdAndNameContaining(memberId,keyword);
    }

    @Override
    public Circle save(Circle circle) {
        return circleJpaRepository.save(circle);
    }

    @Override
    public Optional<Circle> findById(Long circleId) {
        return circleJpaRepository.findById(circleId);
    }

    @Override
    public void delete(Circle circle) {
        circleJpaRepository.delete(circle);
    }
}
