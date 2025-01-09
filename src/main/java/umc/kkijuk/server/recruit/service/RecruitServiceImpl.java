package umc.kkijuk.server.recruit.service;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.common.domian.exception.RecruitOwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.recruit.controller.port.RecruitService;
import umc.kkijuk.server.recruit.domain.RecruitApplyDateUpdate;
import umc.kkijuk.server.recruit.domain.*;
import umc.kkijuk.server.recruit.service.port.RecruitRepository;
import umc.kkijuk.server.review.domain.Review;
import umc.kkijuk.server.review.service.port.ReviewRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class RecruitServiceImpl implements RecruitService {

    private final RecruitRepository recruitRepository;
    private final ReviewRepository reviewRepository;


    @Override
    public Recruit getById(long id) {
        return recruitRepository.findByIdAndIsActive(id, true)
                .orElseThrow(() -> new ResourceNotFoundException("Recruit", id));
    }

    @Override
    @Transactional
    public Recruit create(Member requestMember, RecruitCreate recruitCreate) {
        Recruit recruit = Recruit.from(requestMember.getId(), recruitCreate);
        return recruitRepository.save(recruit);
    }

    @Override
    @Transactional
    public Recruit update(Member requestMember, Long recruitId, RecruitUpdate recruitUpdate) {
        Recruit recruit = getById(recruitId);
        if (!recruit.getMemberId().equals(requestMember.getId())) {
            throw new RecruitOwnerMismatchException();
        }

        recruit = recruit.update(recruitUpdate);
        return recruitRepository.save(recruit);
    }

    @Override
    @Transactional
    public Recruit updateStatus(Member requestMember, long recruitId, RecruitStatusUpdate recruitStatusUpdate) {
        Recruit recruit = getById(recruitId);
        if (!recruit.getMemberId().equals(requestMember.getId())) {
            throw new RecruitOwnerMismatchException();
        }

        recruit = recruit.updateStatus(recruitStatusUpdate);
        return recruitRepository.save(recruit);
    }

    @Override
    @Transactional
    public Recruit disable(Member requestMember, long recruitId) {
        Recruit recruit = getById(recruitId);
        if (!recruit.getMemberId().equals(requestMember.getId())) {
            throw new RecruitOwnerMismatchException();
        }

        recruit = recruit.disable();
        return recruitRepository.save(recruit);
    }

    @Override
    @Transactional
    public Map<Recruit, String> findAllByEndTime(Member requestMember, LocalDate date) {
        List<Recruit> recruits = recruitRepository.findAllActiveRecruitByMemberIdAndEndDate(requestMember.getId(), date);

        Map<Recruit, String> reviewMap = new HashMap<>();

        for (Recruit recruit : recruits) {
            String reviewTitle = reviewRepository.findAllByRecruitId(recruit.getId())
                    .stream().max(Comparator.comparing(Review::getDate))
                    .map(Review::getTitle).orElse("");
            reviewMap.put(recruit,reviewTitle);
        }
        return reviewMap;
    }

    @Override
    @Transactional
    public Map<Recruit, String> findAllByEndTimeAfter(Member requestMember, LocalDateTime endTime) {
        List<Recruit> recruits = recruitRepository.findAllActiveRecruitByMemberIdAndEndTimeAfter(requestMember.getId(), endTime);

        Map<Recruit, String> reviewMap = new HashMap<>();

        for (Recruit recruit : recruits) {
            String reviewTitle = reviewRepository.findAllByRecruitId(recruit.getId())
                .stream().max(Comparator.comparing(Review::getDate))
                .map(Review::getTitle).orElse("");
            reviewMap.put(recruit, reviewTitle);
        }

        return reviewMap;
    }
    @Override
    public List<ValidRecruitDto> findAllValidRecruitByMember(Member requestMember, LocalDateTime endTime) {
        List<Recruit> recruits = recruitRepository.findAllActiveRecruitByMemberId(requestMember.getId());
        return recruits.stream()
                .filter(item -> !isUnappliedOrPlanned(item) || item.getEndTime().isAfter(endTime))
                .map(recruit -> {
                    List<Review> reviews = reviewRepository.findAllByRecruitId(recruit.getId());
                    return ValidRecruitDto.from(reviews!=null?reviews: Collections.emptyList(),recruit);
                }).toList();
    }

    @Override
    public List<RecruitListByMonthDto> findAllValidRecruitByYearAndMonth(Member requestMember, Integer year, Integer month) {
        List<Recruit> recruits = recruitRepository.findAllActiveRecruitByMemberIdAndMonth(requestMember.getId(), year, month);
        return recruits.stream()
                .map(RecruitListByMonthDto::from)
                .toList();
    }

    @Override
    public Recruit updateApplyDate(Member requestMember, long recruitId, RecruitApplyDateUpdate recruitApplyDateUpdate) {
        Recruit recruit = getById(recruitId);
        if (!recruit.getMemberId().equals(requestMember.getId())) {
            throw new RecruitOwnerMismatchException();
        }

        recruit = recruit.updateApplyDate(recruitApplyDateUpdate);
        return recruitRepository.save(recruit);
    }

    @Override
    public List<Recruit> getTopTwoRecruitsByEndTime(Member requestMember) {
        List<Recruit> recruits = recruitRepository.findAllActiveRecruitByMemberIdAndEndTimeAfter(requestMember.getId(), LocalDate.now().atStartOfDay());
        return recruits.stream()
                .sorted(Comparator.comparing(Recruit::getEndTime))
                .limit(2)
                .toList();
    }

    private boolean isUnappliedOrPlanned(Recruit recruit) {
        return recruit.getStatus().equals(RecruitStatus.UNAPPLIED) ||
                recruit.getStatus().equals(RecruitStatus.PLANNED);
    }
}
