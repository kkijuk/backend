package umc.kkijuk.server.career.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.career.domain.EduCareer;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EduCareerRepositoryImpl implements EduCareerRepository{
    private final EduCareerJpaRepository eduCareerJpaRepository;
    @Override
    public List<EduCareer> findByMemberId(Long memberId) {
        return eduCareerJpaRepository.findByMemberId(memberId);
    }

    @Override
    public List<EduCareer> findByMemberIdAndNameContaining(Long memberId, String keyword) {
        return eduCareerJpaRepository.findByMemberIdAndNameContaining(memberId,keyword);
    }

    @Override
    public EduCareer save(EduCareer edu) {
        return eduCareerJpaRepository.save(edu);
    }

    @Override
    public Optional<EduCareer> findById(Long educareerId) {
        return eduCareerJpaRepository.findById(educareerId);
    }

    @Override
    public void delete(EduCareer eduCareer) {
        eduCareerJpaRepository.delete(eduCareer);
    }
}
