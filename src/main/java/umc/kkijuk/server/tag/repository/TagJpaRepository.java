package umc.kkijuk.server.tag.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.kkijuk.server.tag.domain.Tag;
import umc.kkijuk.server.tag.dto.TagResponseDto;

import java.util.List;
import java.util.Optional;
import umc.kkijuk.server.tag.dto.TagUsageResponseDto;

public interface TagJpaRepository extends JpaRepository<Tag,Long> {
    boolean existsByNameAndMemberId(String name, Long memberId);
    List<Tag> findAllTagByMemberId(Long MemberId);
    @Query("SELECT tag FROM Tag tag " +
            "WHERE tag.memberId = :memberId " +
            "AND tag.name LIKE %:keyword% " +
            "ORDER BY CASE WHEN tag.name = :keyword THEN 0 ELSE 1 END, tag.name ASC")
    List<Tag> findByKeywordAndMemberId(@Param("keyword") String keyword, @Param("memberId") Long memberId);
    Optional<Tag> findById(Long Id);

    @Query("""
      select new umc.kkijuk.server.tag.dto.TagUsageResponseDto(
        t.id, t.name, count(cdt.id)
      )
      from Tag t
      left join CareerDetailTag cdt on cdt.tag = t
      left join cdt.baseCareerDetail bcd
      where t.memberId = :memberId
        and (bcd is null or bcd.memberId = :memberId)
      group by t.id, t.name
      order by count(cdt.id) desc, t.name asc
    """)
    List<TagUsageResponseDto> findTopPopularTagsByMember(
        @Param("memberId") Long memberId,
        Pageable pageable
    );



}
