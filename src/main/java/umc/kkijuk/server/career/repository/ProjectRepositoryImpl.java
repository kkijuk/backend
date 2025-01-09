package umc.kkijuk.server.career.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.career.domain.Project;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository{
    private final ProjectJpaRepository projectJpaRepository;
    @Override
    public List<Project> findByMemberId(Long memberId) {
        return projectJpaRepository.findByMemberId(memberId);
    }

    @Override
    public List<Project> findByMemberIdAndNameContaining(Long memberId, String keyword) {
        return projectJpaRepository.findByMemberIdAndNameContaining(memberId,keyword);
    }

    @Override
    public Project save(Project project) {
        return projectJpaRepository.save(project);
    }

    @Override
    public Optional<Project> findById(Long projectId) {
        return projectJpaRepository.findById(projectId);
    }

    @Override
    public void delete(Project project) {
        projectJpaRepository.delete(project);
    }
}
