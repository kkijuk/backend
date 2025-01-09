package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.kkijuk.server.career.domain.Activity;

import java.util.List;

public interface ActivityJpaRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByMemberId(Long memberId);
    List<Activity> findByMemberIdAndNameContaining(Long memberId, String keyword);
}
