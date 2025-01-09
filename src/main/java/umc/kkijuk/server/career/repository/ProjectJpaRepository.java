package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.kkijuk.server.career.domain.Project;

import java.util.List;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {
    List<Project> findByMemberId(Long memberId);
    List<Project> findByMemberIdAndNameContaining(Long memberId, String keyword);
}
