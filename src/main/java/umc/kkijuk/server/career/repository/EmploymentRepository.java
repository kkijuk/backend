package umc.kkijuk.server.career.repository;

import umc.kkijuk.server.career.domain.Employment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmploymentRepository {
    List<Employment> findByMemberId(Long memberId);
    List<Employment> findByMemberIdAndNameContaining(Long memberId, String keyword);

    Employment save(Employment emp);

    Optional<Employment> findById(Long employmentId);

    void delete(Employment employment);
    void updateUnknownEndDates(LocalDate today);
}
