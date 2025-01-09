package umc.kkijuk.server.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.kkijuk.server.tag.domain.Tag;
import umc.kkijuk.server.tag.dto.TagResponseDto;

import java.util.List;
import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<Tag,Long> {
    boolean existsByNameAndMemberId(String name, Long memberId);
    List<Tag> findAllTagByMemberId(Long MemberId);
    @Query("SELECT tag FROM Tag tag " +
            "WHERE tag.memberId = :memberId " +
            "AND tag.name LIKE %:keyword% " +
            "ORDER BY CASE WHEN tag.name = :keyword THEN 0 ELSE 1 END, tag.name ASC")
    List<Tag> findByKeywordAndMemberId(@Param("keyword") String keyword, @Param("memberId") Long memberId);
    Optional<Tag> findById(Long Id);
}
