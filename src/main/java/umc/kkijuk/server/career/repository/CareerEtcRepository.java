package umc.kkijuk.server.career.repository;

import umc.kkijuk.server.career.domain.CareerEtc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CareerEtcRepository {
    List<CareerEtc> findByMemberId(Long memberId);

    List<CareerEtc> findByMemberIdAndNameContaining(Long id, String keyword);

    CareerEtc save(CareerEtc etc);

    Optional<CareerEtc> findById(Long etcId);

    void delete(CareerEtc etc);
    void updateUnknownEndDates(LocalDate today);
}
