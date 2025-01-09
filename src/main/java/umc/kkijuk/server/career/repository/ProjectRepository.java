package umc.kkijuk.server.career.repository;

import umc.kkijuk.server.career.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findByMemberId(Long memberId);
    List<Project> findByMemberIdAndNameContaining(Long memberId, String keyword);

    Project save(Project project);

    Optional<Project> findById(Long projectId);

    void delete(Project project);
}
