package umc.kkijuk.server.recruit.controller.port;

import java.util.Map;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.recruit.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RecruitService {
    Recruit create(Member member, RecruitCreate recruitCreate);

    Recruit update(Member member, Long recruitId, RecruitUpdate recruitUpdate);

    Recruit getById(long id);

    Recruit updateStatus(Member member, long recruitId, RecruitStatusUpdate recruitStatusUpdate);

    Recruit disable(Member member, long recruitId);

    Map<Recruit, String> findAllByEndTime(Member member, LocalDate date);

    Map<Recruit, String> findAllByEndTimeAfter(Member member, LocalDateTime endTime);

    List<ValidRecruitDto> findAllValidRecruitByMember(Member member, LocalDateTime endTime);

    List<RecruitListByMonthDto> findAllValidRecruitByYearAndMonth(Member member, Integer year, Integer month);

    Recruit updateApplyDate(Member requestMember, long recruitId, RecruitApplyDateUpdate recruitApplyDateUpdate);

    List<Recruit> getTopTwoRecruitsByEndTime(Member requestMember);
}
