package umc.kkijuk.server.career.repository;

import umc.kkijuk.server.career.domain.Activity;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository {
    List<Activity> findByMemberId(Long memberId);
    List<Activity> findByMemberIdAndNameContaining(Long memberId, String keyword);
    Activity save(Activity activity);

    Optional<Activity> findById(Long activityId);

    void delete(Activity activity);
}
