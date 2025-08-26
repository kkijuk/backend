package umc.kkijuk.server.tag.repository;

import org.springframework.data.domain.Pageable;
import umc.kkijuk.server.tag.domain.Tag;

import java.util.List;
import java.util.Optional;
import umc.kkijuk.server.tag.dto.TagUsageResponseDto;

public interface TagRepository {
    boolean existsByNameAndMemberId(String tagName, Long memberId);

    Tag save(Tag newTag);

    List<Tag> findAllTagByMemberId(Long id);

    Optional<Tag> findById(Long tagId);

    void delete(Tag deleteTag);

    List<Tag> findByKeywordAndMemberId(String keyword, Long id);
    List<TagUsageResponseDto> findPopularTagsByMemberAndKeyword(Long memberId, String keyword, Pageable pageable);
}
