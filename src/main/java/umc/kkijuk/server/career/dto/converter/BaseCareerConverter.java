package umc.kkijuk.server.career.dto.converter;

import umc.kkijuk.server.career.controller.response.FindTagResponse;
import umc.kkijuk.server.career.domain.*;
import umc.kkijuk.server.career.dto.*;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.tag.domain.Tag;

import java.util.List;
import java.util.stream.Collectors;

public class BaseCareerConverter {
    public static EduCareer toEduCareer(Member requestMember, EduCareerReqDto eduCareerReqDto) {
        return EduCareer.builder()
                .memberId(requestMember.getId())
                .name(eduCareerReqDto.getName())
                .alias(eduCareerReqDto.getAlias())
                .unknown(eduCareerReqDto.getUnknown())
                .startdate(eduCareerReqDto.getStartdate())
                .enddate(eduCareerReqDto.getEnddate())
                .organizer(eduCareerReqDto.getOrganizer())
                .time(eduCareerReqDto.getTime())
                .build();

    }
    public static Employment toEmployment(Member requestMember, EmploymentReqDto employmentReqDto) {
        return Employment.builder()
                .memberId(requestMember.getId())
                .name(employmentReqDto.getName())
                .alias(employmentReqDto.getAlias())
                .unknown(employmentReqDto.getUnknown())
                .startdate(employmentReqDto.getStartdate())
                .enddate(employmentReqDto.getEnddate())
                .type(employmentReqDto.getType())
                .position(employmentReqDto.getPosition())
                .field(employmentReqDto.getField())
                .build();
    }
    public static Project toProject(Member requestMember, ProjectReqDto projectReqDto) {
        return Project.builder()
                .memberId(requestMember.getId())
                .name(projectReqDto.getName())
                .alias(projectReqDto.getAlias())
                .unknown(projectReqDto.getUnknown())
                .startdate(projectReqDto.getStartdate())
                .enddate(projectReqDto.getEnddate())
                .teamSize(projectReqDto.getTeamSize())
                .isTeam(projectReqDto.getIsTeam())
                .contribution(projectReqDto.getContribution())
                .location(projectReqDto.getLocation()).build();

    }

    public static Competition toCompetition(Member requestMember, CompetitionReqDto competitionReqDto) {
        return Competition.builder()
                .memberId(requestMember.getId())
                .name(competitionReqDto.getName())
                .alias(competitionReqDto.getAlias())
                .unknown(competitionReqDto.getUnknown())
                .startdate(competitionReqDto.getStartdate())
                .enddate(competitionReqDto.getEnddate())
                .organizer(competitionReqDto.getOrganizer())
                .teamSize(competitionReqDto.getTeamSize())
                .contribution(competitionReqDto.getContribution())
                .isTeam(competitionReqDto.getIsTeam()).build();

    }

    public static Activity toAvtivity(Member requestMember, ActivityReqDto activityReqDto) {
        return Activity.builder()
                .memberId(requestMember.getId())
                .name(activityReqDto.getName())
                .alias(activityReqDto.getAlias())
                .unknown(activityReqDto.getUnknown())
                .startDate(activityReqDto.getStartdate())
                .endDate(activityReqDto.getEnddate())
                .organizer(activityReqDto.getOrganizer())
                .role(activityReqDto.getRole())
                .contribution(activityReqDto.getContribution())
                .teamSize(activityReqDto.getTeamSize())
                .isTeam(activityReqDto.getIsTeam())
                .build();
    }

    public static Circle toCircle(Member requestMember, CircleReqDto circleReqDto) {
        return Circle.builder()
                .memberId(requestMember.getId())
                .name(circleReqDto.getName())
                .alias(circleReqDto.getAlias())
                .unknown(circleReqDto.getUnknown())
                .startdate(circleReqDto.getStartdate())
                .enddate(circleReqDto.getEnddate())
                .location(circleReqDto.getLocation())
                .role(circleReqDto.getRole()).build();
    }

    public static CareerEtc toEtc(Member requestMember, EtcReqDto etcReqDto) {
        return CareerEtc.builder()
                .memberId(requestMember.getId())
                .name(etcReqDto.getName())
                .alias(etcReqDto.getAlias())
                .unknown(etcReqDto.getUnknown())
                .startdate(etcReqDto.getStartdate())
                .enddate(etcReqDto.getEnddate())
                .build();
    }

    public static FindTagResponse.SearchTagResponse toSearchTagResponse(List<Tag> tags, int detailCount) {
        List<FindTagResponse.TagResponse> tagList = tags.stream().map(tag-> FindTagResponse.TagResponse.builder()
                        .tagId(tag.getId())
                        .tagName(tag.getName()).build()).collect(Collectors.toList());
        return FindTagResponse.SearchTagResponse.builder()
                .detailCount(detailCount)
                .tagList(tagList)
                .build();
    }
}
