package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.kkijuk.server.career.domain.CareerEtc;

import java.util.List;

public interface CareerEtcJpaRepository extends JpaRepository<CareerEtc,Long> {
    List<CareerEtc> findByMemberId(Long memberId);

    List<CareerEtc> findByMemberIdAndNameContaining(Long id, String keyword);
}
