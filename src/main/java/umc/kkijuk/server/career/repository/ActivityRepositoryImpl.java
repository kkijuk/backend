package umc.kkijuk.server.career.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.career.domain.Activity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepository{
    private final ActivityJpaRepository activityJpaRepository;
    @Override
    public List<Activity> findByMemberId(Long memberId) {
        return activityJpaRepository.findByMemberId(memberId);
    }
    @Override
    public List<Activity> findByMemberIdAndNameContaining(Long memberId, String keyword) {
        return activityJpaRepository.findByMemberIdAndNameContaining(memberId,keyword);
    }
    @Override
    public Activity save(Activity activity) {
        return activityJpaRepository.save(activity);
    }
    @Override
    public Optional<Activity> findById(Long activityId) {
        return activityJpaRepository.findById(activityId);
    }

    @Override
    public void delete(Activity activity) {
        activityJpaRepository.delete(activity);
    }
}
