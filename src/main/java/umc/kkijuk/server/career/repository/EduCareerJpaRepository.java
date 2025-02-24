package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.kkijuk.server.career.domain.EduCareer;

import java.util.List;

public interface EduCareerJpaRepository extends JpaRepository<EduCareer,Long> {
    List<EduCareer> findByMemberId(Long memberId);
    @Query("SELECT edu FROM EduCareer edu WHERE edu.memberId = :memberId "
        + "AND (edu.name LIKE %:keyword% "
        + "OR edu.alias LIKE %:keyword%)")
    List<EduCareer> findByMemberIdAndNameContaining(Long memberId, String keyword);
}
