package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.career.domain.Activity;

import java.time.LocalDate;
import java.util.List;

public interface ActivityJpaRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByMemberId(Long memberId);

    @Query("SELECT a FROM Activity a WHERE a.memberId = :memberId "
            + "AND (a.name LIKE %:keyword% "
            + "OR a.alias LIKE %:keyword%)")
    List<Activity> findByMemberIdAndNameContaining(Long memberId, String keyword);

    @Modifying(clearAutomatically=true)
    @Query("UPDATE Activity a SET a.enddate = :today WHERE a.unknown = true")
    void updateUnknownEndDates(LocalDate today);
}
