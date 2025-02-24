package umc.kkijuk.server.career.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.kkijuk.server.career.domain.CareerEtc;

import java.util.List;

public interface CareerEtcJpaRepository extends JpaRepository<CareerEtc,Long> {
    List<CareerEtc> findByMemberId(Long memberId);

    @Query("SELECT etc FROM CareerEtc etc WHERE etc.memberId = :memberId "
        + "AND (etc.name LIKE %:keyword% "
        + "OR etc.alias LIKE %:keyword%)")
    List<CareerEtc> findByMemberIdAndNameContaining(Long memberId, String keyword);
}
