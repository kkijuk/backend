package umc.kkijuk.server.tag.repository;

import umc.kkijuk.server.tag.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    boolean existsByNameAndMemberId(String tagName, Long memberId);

    Tag save(Tag newTag);

    List<Tag> findAllTagByMemberId(Long id);

    Optional<Tag> findById(Long tagId);

    void delete(Tag deleteTag);

    List<Tag> findByKeywordAndMemberId(String keyword, Long id);
}
