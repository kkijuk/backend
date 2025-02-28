package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.career.domain.Employment;

import java.time.LocalDate;
import java.util.List;

public interface EmploymentJpaRepository extends JpaRepository<Employment,Long> {
    List<Employment> findByMemberId(Long memberId);
    @Query("SELECT emp FROM Employment emp WHERE emp.memberId = :memberId "
        + "AND (emp.name LIKE %:keyword% "
        + "OR emp.alias LIKE %:keyword%)")
    List<Employment> findByMemberIdAndNameContaining(Long memberId, String keyword);

    @Modifying(clearAutomatically=true)
    @Query("UPDATE Employment a SET a.enddate = :today WHERE a.unknown = true")
    void updateUnknownEndDates(LocalDate today);
}
