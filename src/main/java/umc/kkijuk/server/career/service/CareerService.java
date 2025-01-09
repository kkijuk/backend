package umc.kkijuk.server.career.service;

import umc.kkijuk.server.career.controller.response.*;
import umc.kkijuk.server.career.dto.*;
import umc.kkijuk.server.member.domain.Member;

import java.util.List;
import java.util.Map;

public interface CareerService {
    ActivityResponse createActivity(Member requestMember, ActivityReqDto activityReqDto);

    CircleResponse createCircle(Member requestMember, CircleReqDto circleReqDto);

    CompetitionResponse createCompetition(Member requestMember, CompetitionReqDto competitionReqDto);

    EduCareerResponse crateEduCareer(Member requestMember, EduCareerReqDto eduCareerReqDto);

    EmploymentResponse createEmployment(Member requestMember, EmploymentReqDto employmentReqDto);

    ProjectResponse createProject(Member requestMember, ProjectReqDto projectReqDto);
    EtcResponse createEtc(Member requestMember, EtcReqDto etcReqDto);

    void deleteActivity(Member requestMember, Long activityId);

    void deleteCircle(Member requestMember, Long circleId);

    void deleteComp(Member requestMember, Long competitionId);

    void deleteEdu(Member requestMember, Long educareerId);

    void deleteEmp(Member requestMember, Long employmentId);

    void deleteProject(Member requestMember, Long projectId);
    void deleteEtc(Member requestMember, Long etcId);

    ActivityResponse updateActivity(Member requestMember, Long activityId, ActivityReqDto request);

    CircleResponse updateCircle(Member requestMember, Long circleId, CircleReqDto circleReqDto);

    CompetitionResponse updateComp(Member requestMember, Long competitionId, CompetitionReqDto competitionReqDto);

    EduCareerResponse updateEdu(Member requestMember, Long educareerId, EduCareerReqDto eduCareerReqDto);

    EmploymentResponse updateEmp(Member requestMember, Long employmentId, EmploymentReqDto employmentReqDto);
    EtcResponse updateEtc(Member requestMember, Long etcId, EtcReqDto etcReqDto);

    ProjectResponse updateProject(Member requestMember, Long projectId, ProjectReqDto projectReqDto);

    void deleteBaseCareer(Member requestMember, Long careerId, String type);
    BaseCareerResponse createSummary(Member requestMember, Long careerId, CareerSummaryReqDto request);

//    Map<String, List<?>>  findAllCareerGroupedCategory(Long id);
//    Map<String, List<?>> findAllCareerGroupedYear(Long id);

//    BaseCareerResponse findCareer(Member requestMember, Long careerId, String type);

//    List<FindDetailResponse> findAllDetail(Member requestMember, String keyword, String sort);
//
//    List<FindTagResponse> findAllTag(Member requestMember, String keyword);
//
//    List<FindDetailResponse> findAllDetailByTag(Member requestMember, Long tagId, String sort);
//
//    List<FindCareerResponse> findCareerWithKeyword(Member requestMember, String keyword, String sort);

//    List<TimelineResponse> findCareerForTimeline(Member requestMember);

//    List<BaseCareerResponse> findAllCareer(Long memberId);
}
