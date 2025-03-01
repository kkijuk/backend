package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.career.domain.Project;

import java.time.LocalDate;
import java.util.List;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {
    List<Project> findByMemberId(Long memberId);
    @Query("SELECT project FROM Project project WHERE project.memberId = :memberId "
        + "AND (project.name LIKE %:keyword% "
        + "OR project.alias LIKE %:keyword%)")
    List<Project> findByMemberIdAndNameContaining(Long memberId, String keyword);

    @Modifying(clearAutomatically=true)
    @Query("UPDATE Project a SET a.enddate = :today WHERE a.unknown = true")
    void updateUnknownEndDates(LocalDate today);
}
