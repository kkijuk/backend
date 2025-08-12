package umc.kkijuk.server.introduce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.kkijuk.server.introduce.domain.Introduce;
import umc.kkijuk.server.introduce.domain.MasterIntroduce;

import java.util.List;
import java.util.Optional;

public interface MasterIntroduceRepository extends JpaRepository<MasterIntroduce, Long> {
    Optional<MasterIntroduce> findByMemberId(Long memberId);

    @Query("""
    SELECT DISTINCT m
    FROM MasterIntroduce m
    JOIN FETCH m.masterQuestion q
    WHERE m.memberId = :memberId
      AND LOWER(CAST(q.content AS string)) LIKE CONCAT('%', LOWER(:keyword), '%')
    """)
    List<MasterIntroduce> searchMasterIntroduceByKeywordForMember(@Param("keyword") String keyword, @Param("memberId") Long memberId);

}
