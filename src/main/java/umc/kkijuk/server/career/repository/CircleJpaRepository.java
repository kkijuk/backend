package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.kkijuk.server.career.domain.Circle;

import java.util.List;

public interface CircleJpaRepository extends JpaRepository<Circle, Long> {
    List<Circle> findByMemberId(Long memberId);
    List<Circle> findByMemberIdAndNameContaining(Long memberId, String keyword);
}
