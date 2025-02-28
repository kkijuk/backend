package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.career.domain.EduCareer;

import java.time.LocalDate;
import java.util.List;

public interface EduCareerJpaRepository extends JpaRepository<EduCareer,Long> {
    List<EduCareer> findByMemberId(Long memberId);
    @Query("SELECT edu FROM EduCareer edu WHERE edu.memberId = :memberId "
        + "AND (edu.name LIKE %:keyword% "
        + "OR edu.alias LIKE %:keyword%)")
    List<EduCareer> findByMemberIdAndNameContaining(Long memberId, String keyword);

    @Modifying(clearAutomatically=true)
    @Query("UPDATE EduCareer a SET a.enddate = :today WHERE a.unknown = true")
    void updateUnknownEndDates(LocalDate today);
}
