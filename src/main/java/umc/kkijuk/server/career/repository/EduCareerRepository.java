package umc.kkijuk.server.career.repository;

import umc.kkijuk.server.career.domain.EduCareer;

import java.util.List;
import java.util.Optional;

public interface EduCareerRepository {
    List<EduCareer> findByMemberId(Long memberId);
    List<EduCareer> findByMemberIdAndNameContaining(Long memberId, String keyword);

    EduCareer save(EduCareer edu);

    Optional<EduCareer> findById(Long educareerId);

    void delete(EduCareer eduCareer);
}
