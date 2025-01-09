package umc.kkijuk.server.tag.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.tag.domain.Tag;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository{
    private final TagJpaRepository tagJpaRepository;

    @Override
    public boolean existsByNameAndMemberId(String tagName, Long memberId) {
        return tagJpaRepository.existsByNameAndMemberId(tagName,memberId);
    }

    @Override
    public Tag save(Tag newTag) {
        return tagJpaRepository.save(newTag);
    }

    @Override
    public List<Tag> findAllTagByMemberId(Long id) {
        return tagJpaRepository.findAllTagByMemberId(id);
    }

    @Override
    public Optional<Tag> findById(Long tagId) {
        return tagJpaRepository.findById(tagId);
    }

    @Override
    public void delete(Tag deleteTag) {
        tagJpaRepository.delete(deleteTag);
    }

    @Override
    public List<Tag> findByKeywordAndMemberId(String keyword, Long id) {
        return tagJpaRepository.findByKeywordAndMemberId(keyword,id);
    }
}
