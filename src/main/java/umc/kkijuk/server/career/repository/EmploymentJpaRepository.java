package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.kkijuk.server.career.domain.Employment;

import java.util.List;

public interface EmploymentJpaRepository extends JpaRepository<Employment,Long> {
    List<Employment> findByMemberId(Long memberId);
    List<Employment> findByMemberIdAndNameContaining(Long memberId, String keyword);
}
