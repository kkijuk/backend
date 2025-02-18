package umc.kkijuk.server.career.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.career.controller.response.*;
import umc.kkijuk.server.career.dto.*;
import umc.kkijuk.server.career.service.CareerService;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;
import umc.kkijuk.server.common.LoginUser;



@Tag(name="Career",description = "활동 생성, 수정, 삭제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/career")
public class BaseCareerController {
    private final CareerService baseCareerService;
    private final MemberService memberService;
    private final LoginUser loginUser;

    @PostMapping("/activity")
    @Operation(summary = "커리어(대외활동) 생성", description = "주어진 정보를 바탕으로 활동을 추가합니다.")
    public CareerResponse<ActivityResponse> createActivity(@RequestHeader("Authorization") String token,
            @RequestBody @Valid ActivityReqDto activityReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);

        return CareerResponse.success(
                CareerResponseMessage.CAREER_CREATE_SUCCESS,
                baseCareerService.createActivity(requestMember, activityReqDto)
        );
    }

    @PatchMapping("/activity/{activityId}")
    @Operation(summary = "커리어(대외활동) 수정", description = "활동 ID에 해당하는 활동을 수정합니다.")
    @Parameter(name = "activityId", description = "커리어(대외활동) Id, path variable 입니다.", example = "1")
    public CareerResponse<ActivityResponse> updateActivity(
            @RequestHeader("Authorization") String token,
            @PathVariable Long activityId,
            @Valid @RequestBody ActivityReqDto activityReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_UPDATE_SUCCESS,
                baseCareerService.updateActivity(requestMember, activityId,  activityReqDto)
        );
    }

    @PostMapping("/circle")
    @Operation(summary = "커리어(동아리) 생성", description = "주어진 정보를 바탕으로 활동을 추가합니다.")
    public CareerResponse<CircleResponse> createCircle(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CircleReqDto circleReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_CREATE_SUCCESS,
                baseCareerService.createCircle(requestMember, circleReqDto)
        );
    }

    @PatchMapping("/circle/{circleId}")
    @Operation(summary = "커리어(동아리) 수정", description = "활동 ID에 해당하는 활동을 수정합니다.")
    @Parameter(name = "circleId", description = "커리어(동아리) Id, path variable 입니다.", example = "1")
    public CareerResponse<CircleResponse> updateCircle(
            @RequestHeader("Authorization") String token,
            @PathVariable Long circleId,
            @Valid @RequestBody CircleReqDto circleReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_UPDATE_SUCCESS,
                baseCareerService.updateCircle(requestMember, circleId, circleReqDto)
        );
    }

    @PostMapping("/competition")
    @Operation(summary = "커리어(대회) 생성", description = "주어진 정보를 바탕으로 활동을 추가합니다.")
    public CareerResponse<CompetitionResponse> createComp(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CompetitionReqDto competitionReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_CREATE_SUCCESS,
                baseCareerService.createCompetition(requestMember, competitionReqDto)
        );
    }

    @PatchMapping("/competition/{competitionId}")
    @Operation(summary = "커리어(대회) 수정", description = "활동 ID에 해당하는 활동을 수정합니다.")
    @Parameter(name = "competitionId", description = "커리어(대회) Id, path variable 입니다.", example = "1")
    public CareerResponse<CompetitionResponse> updateComp(
            @RequestHeader("Authorization") String token,
            @PathVariable Long competitionId,
            @Valid @RequestBody CompetitionReqDto competitionReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_UPDATE_SUCCESS,
                baseCareerService.updateComp(requestMember, competitionId, competitionReqDto)
        );
    }

    @PostMapping("/educareer")
    @Operation(summary = "커리어(교육) 생성", description = "주어진 정보를 바탕으로 활동을 추가합니다.")
    public CareerResponse<EduCareerResponse> createEdu(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody EduCareerReqDto eduCareerReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_CREATE_SUCCESS,
                baseCareerService.crateEduCareer(requestMember, eduCareerReqDto)
        );
    }

    @PatchMapping("/educareer/{educareerId}")
    @Operation(summary = "커리어(교육) 수정", description = "활동 ID에 해당하는 활동을 수정합니다.")
    @Parameter(name = "educareerId", description = "커리어(교육) Id, path variable 입니다.", example = "1")
    public CareerResponse<EduCareerResponse> updateEdu(
            @RequestHeader("Authorization") String token,
            @PathVariable Long educareerId,
            @Valid @RequestBody EduCareerReqDto eduCareerReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_UPDATE_SUCCESS,
                baseCareerService.updateEdu(requestMember, educareerId, eduCareerReqDto)
        );
    }

    @PostMapping("/employment")
    @Operation(summary = "커리어(경력) 생성", description = "주어진 정보를 바탕으로 활동을 추가합니다.")
    public CareerResponse<EmploymentResponse> createEmp(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody EmploymentReqDto employmentReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_CREATE_SUCCESS,
                baseCareerService.createEmployment(requestMember, employmentReqDto)
        );
    }

    @PatchMapping("/employment/{employmentId}")
    @Operation(summary = "커리어(경력) 수정", description = "활동 ID에 해당하는 활동을 수정합니다.")
    @Parameter(name = "employmentId", description = "커리어(아르바이트/인턴) Id, path variable 입니다.", example = "1")
    public CareerResponse<EmploymentResponse> updateEmp(
            @RequestHeader("Authorization") String token,
            @PathVariable Long employmentId,
            @Valid @RequestBody EmploymentReqDto employmentReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_UPDATE_SUCCESS,
                baseCareerService.updateEmp(requestMember, employmentId, employmentReqDto)
        );
    }

    @PostMapping("/project")
    @Operation(summary = "커리어(프로젝트) 생성", description = "주어진 정보를 바탕으로 활동을 추가합니다.")
    public CareerResponse<ProjectResponse> createProject(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ProjectReqDto projectReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_CREATE_SUCCESS,
                baseCareerService.createProject(requestMember, projectReqDto)
        );
    }

    @PatchMapping("/project/{projectId}")
    @Operation(summary = "커리어(프로젝트) 수정", description = "활동 ID에 해당하는 활동을 수정합니다.")
    @Parameter(name = "projectId", description = "커리어(프로젝트) Id, path variable 입니다.", example = "1")
    public CareerResponse<ProjectResponse> updateProject(
            @RequestHeader("Authorization") String token,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectReqDto projectReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_UPDATE_SUCCESS,
                baseCareerService.updateProject(requestMember, projectId, projectReqDto)
        );
    }


    @PostMapping("/etc")
    @Operation(summary = "커리어(기타) 생성", description = "주어진 정보를 바탕으로 활동을 추가합니다.")
    public CareerResponse<EtcResponse> createEtc(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody EtcReqDto etcReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_CREATE_SUCCESS,
                baseCareerService.createEtc(requestMember, etcReqDto)
        );
    }

    @PatchMapping("/etc/{etcId}")
    @Operation(summary = "커리어(기타) 수정", description = "활동 ID에 해당하는 활동을 수정합니다.")
    @Parameter(name="etcId", description = "커리어(기타) Id, path variable 입니다.",example = "1")
    public CareerResponse<EtcResponse> updateEtc(
            @RequestHeader("Authorization") String token,
            @PathVariable Long etcId,
            @Valid @RequestBody EtcReqDto etcReqDto
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_UPDATE_SUCCESS,
                baseCareerService.updateEtc(requestMember, etcId, etcReqDto)
        );
    }

    @DeleteMapping("/{type}/{careerId}")
    @Operation(summary = "커리어 삭제", description = "활동의 type과 ID에 해당하는 활동을 삭제합니다.")
    public CareerResponse<Object> deleteBaseCareer(
            @RequestHeader("Authorization") String token,
            @PathVariable String type,
            @PathVariable Long careerId
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        baseCareerService.deleteBaseCareer(requestMember, careerId, type);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_DELETE_SUCCESS,
                null
        );
    }

    @PatchMapping("/{careerId}")
    @Operation(summary = "활동 내역 수정", description = "활동 ID에 해당하는 활동에 활동 내역을 추가합니다.")
    @Parameter(name = "careerId", description = "활동 Id, path variable 입니다.", example = "1")
    public CareerResponse<BaseCareerResponse> createSummary(
            @RequestHeader("Authorization") String token,
            @PathVariable Long careerId,
            @Valid @RequestBody CareerSummaryReqDto request
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_CREATE_SUCCESS,
                baseCareerService.createSummary(requestMember, careerId, request)
        );
    }
}
