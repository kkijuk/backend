package umc.kkijuk.server.career.repository;

import umc.kkijuk.server.career.domain.Circle;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CircleRepository {
    List<Circle> findByMemberId(Long memberId);
    List<Circle> findByMemberIdAndNameContaining(Long memberId, String keyword);

    Circle save(Circle circle);

    Optional<Circle> findById(Long circleId);

    void delete(Circle circle);
    void updateUnknownEndDates(LocalDate today);
}
