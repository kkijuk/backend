package umc.kkijuk.server.career.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.career.domain.Employment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmploymentRepositoryImpl implements EmploymentRepository{
    private final EmploymentJpaRepository employmentJpaRepository;
    @Override
    public List<Employment> findByMemberId(Long memberId) {
        return employmentJpaRepository.findByMemberId(memberId);
    }

    @Override
    public List<Employment> findByMemberIdAndNameContaining(Long memberId, String keyword) {
        return employmentJpaRepository.findByMemberIdAndNameContaining(memberId, keyword);
    }

    @Override
    public Employment save(Employment emp) {
        return employmentJpaRepository.save(emp);
    }

    @Override
    public Optional<Employment> findById(Long employmentId) {
        return employmentJpaRepository.findById(employmentId);
    }

    @Override
    public void delete(Employment employment) {
        employmentJpaRepository.delete(employment);
    }
}
