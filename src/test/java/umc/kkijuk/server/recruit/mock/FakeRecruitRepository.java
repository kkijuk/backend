package umc.kkijuk.server.recruit.mock;

import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.recruit.domain.Recruit;
import umc.kkijuk.server.recruit.service.port.RecruitRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeRecruitRepository implements RecruitRepository {
    private final AtomicLong authGeneratedID = new AtomicLong(0);
    private final List<Recruit> data = new ArrayList<>();

    @Override
    public Optional<Recruit> findById(Long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny();
    }

    @Override
    public Recruit save(Recruit recruit) {
        if (recruit.getId() == null || recruit.getId() == 0){
            Recruit newRecruit = Recruit.builder()
                    .id(authGeneratedID.incrementAndGet())
                    .title(recruit.getTitle())
                    .status(recruit.getStatus())
                    .startTime(recruit.getStartTime())
                    .endTime(recruit.getEndTime())
                    .applyDate(recruit.getApplyDate())
                    .tags(recruit.getTags())
                    .link(recruit.getLink())
                    .isActive(recruit.getIsActive())
                    .build();
            data.add(newRecruit);
            return newRecruit;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), recruit.getId()));
            data.add(recruit);
            return recruit;
        }
    }

    @Override
    public Recruit getById(long id) {
        return findByIdAndIsActive(id, true)
                .orElseThrow(() -> new ResourceNotFoundException("recruit", id)
        );
    }

    @Override
    public Optional<Recruit> findByIdAndIsActive(long id, Boolean isActive) {
        return data.stream()
                .filter(item ->
                        item.getId().equals(id) &&
                        item.getIsActive() != null &&
                        item.getIsActive()).findAny();
    }

    @Override
    public List<Recruit> findAllByEndDateAndIsActive(LocalDate endTime, Boolean isActive) {
        return data.stream()
                .filter(item ->
                        endTime.equals(item.getEndTime().toLocalDate()) &&
                        item.getIsActive() != null &&
                        item.getIsActive()).toList();
    }
}
