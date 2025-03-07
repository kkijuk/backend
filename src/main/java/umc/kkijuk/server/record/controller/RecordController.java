package umc.kkijuk.server.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.common.LoginUser;
import umc.kkijuk.server.introduce.common.BaseResponse;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;
import umc.kkijuk.server.record.controller.response.*;
import umc.kkijuk.server.record.dto.*;
import umc.kkijuk.server.record.service.RecordService;

import java.util.List;

@Tag(name = "record", description = "이력서 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/history/resume")
public class RecordController {
    private final RecordService recordService;
    private final MemberService memberService;
    private final LoginUser loginUser;

    @PostMapping
    @Operation(summary = "이력서 생성")
    public ResponseEntity<Object> save(@RequestHeader("Authorization") String token,
                                       @RequestBody RecordReqDto recordReqDto) {
        Member requestMember = loginUser.extractMemberId(token);
        RecordResponse recordResponse = recordService.saveRecord(requestMember, recordReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "이력서 생성 완료", recordResponse));
    }

    @GetMapping
    @Operation(summary = "이력서 전체 조회")
    public ResponseEntity<Object> get(@RequestHeader("Authorization") String token) {
        Member requestMember = loginUser.extractMemberId(token);
        Long memberId = requestMember.getId();
        RecordResponse recordResponse = recordService.getRecord(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "이력서 전체 조회 완료", recordResponse));
    }

    @PatchMapping
    @Operation(summary = "이력서 정보 수정")
    public ResponseEntity<Object> update(@RequestHeader("Authorization") String token,
                                         @Valid @RequestBody RecordReqDto recordReqDto) {
        Member requestMember = loginUser.extractMemberId(token);
        Long memberId = requestMember.getId();
        RecordResponse recordResponse = recordService.updateRecord(memberId,
                recordService.findByMemberId(memberId).getId(), recordReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "이력서 수정 완료", recordResponse));
    }

    @GetMapping("/download")
    @Operation(summary = "이력서 내보내기", description = "이력서 내보내기에 필요한 정보들을 조회합니다.")
    public ResponseEntity<Object> downloadResume(@RequestHeader("Authorization") String token) {
        Member requestMember = loginUser.extractMemberId(token);
        Long memberId = requestMember.getId();
        RecordDownResponse response = recordService.downloadResume(recordService.findByMemberId(memberId).getId(), memberId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "이력서 내보내기 정보 조회 완료", response));
    }

    @PostMapping("/education")
    @Operation(summary = "학력 생성")
    public ResponseEntity<Object> saveEducation(@RequestHeader("Authorization") String token,
                                                @Valid @RequestBody EducationReqDto educationReqDto) {
        Member requestMember = loginUser.extractMemberId(token);
        Long memberId = requestMember.getId();
        List<EducationResponse> educationResponse = recordService.saveEducation(requestMember,
                recordService.findByMemberId(memberId).getId(), educationReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "학력 생성 완료", educationResponse));
    }

    @PatchMapping("/education")
    @Operation(summary = "학력 수정")
    public ResponseEntity<Object> patchEducation(@RequestHeader("Authorization") String token,
                                                 Long educationId,
                                                 @Valid @RequestBody EducationReqDto educationReqDto) {
        Member requestMember = loginUser.extractMemberId(token);
        List<EducationResponse> educationResponse = recordService.updateEducation(requestMember, educationId, educationReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "학력 수정 완료", educationResponse));
    }

    @DeleteMapping("/education")
    @Operation(summary = "학력 삭제")
    public ResponseEntity<Object> deleteEducation(@RequestHeader("Authorization") String token,
                                                  Long educationId) {
        Member requestMember = loginUser.extractMemberId(token);
        List<EducationResponse> educationResponse = recordService.deleteEducation(requestMember, educationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "학력 삭제 완료", educationResponse));
    }

    @PostMapping("/license")
    @Operation(summary = "자격증 생성")
    public ResponseEntity<Object> saveLicense(@RequestHeader("Authorization") String token,
                                              @Valid @RequestBody LicenseReqDto licenseReqDto) {
        Member requestMember = loginUser.extractMemberId(token);

        List<LicenseResponse> licenseResponse = recordService.saveLicense(requestMember,
                recordService.findByMemberId(requestMember.getId()).getId(), licenseReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자격증 생성 완료", licenseResponse));
    }

    @PatchMapping("/license")
    @Operation(summary = "자격증 수정")
    public ResponseEntity<Object> patchLicense(@RequestHeader("Authorization") String token,
                                               Long licenseId,
                                               @Valid @RequestBody LicenseReqDto licenseReqDto) {
        Member requestMember = loginUser.extractMemberId(token);
        List<LicenseResponse> licenseResponse = recordService.updateLicense(requestMember, licenseId, licenseReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자격증 수정 완료", licenseResponse));
    }

    @DeleteMapping("/license")
    @Operation(summary = "자격증 삭제")
    public ResponseEntity<Object> deleteLicense(@RequestHeader("Authorization") String token,
                                                Long licenseId) {
        Member requestMember = loginUser.extractMemberId(token);
        List<LicenseResponse> licenseResponses = recordService.deleteLicense(requestMember, licenseId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자격증 삭제 완료", licenseResponses));
    }

    @PostMapping("/award")
    @Operation(summary = "수상 생성")
    public ResponseEntity<Object> saveAward(@RequestHeader("Authorization") String token,
                                            @Valid @RequestBody AwardReqDto awardReqDto) {
        Member requestMember = loginUser.extractMemberId(token);
        List<AwardResponse> awardResponse = recordService.saveAward(requestMember,
                recordService.findByMemberId(requestMember.getId()).getId(), awardReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "수상 생성 완료", awardResponse));
    }

    @PatchMapping("/award")
    @Operation(summary = "수상 수정")
    public ResponseEntity<Object> patchAward(@RequestHeader("Authorization") String token,
                                             Long awardId,
                                             @Valid @RequestBody AwardReqDto awardReqDto) {
        Member requestMember = loginUser.extractMemberId(token);
        List<AwardResponse> awardResponse = recordService.updateAward(requestMember, awardId, awardReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "수상 수정 완료", awardResponse));
    }

    @DeleteMapping("/award")
    @Operation(summary = "수상 삭제")
    public ResponseEntity<Object> deleteAward(@RequestHeader("Authorization") String token,
                                              Long awardId) {
        Member requestMember = loginUser.extractMemberId(token);
        List<AwardResponse> awardResponse = recordService.deleteAward(requestMember, awardId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "수상 삭제 완료", awardResponse));
    }

    @PostMapping("/skill")
    @Operation(summary = "스킬 생성")
    public ResponseEntity<Object> saveSkill(@RequestHeader("Authorization") String token,
                                            @Valid @RequestBody SkillReqDto skillReqDto) {
        Member requestMember = loginUser.extractMemberId(token);
        List<SkillResponse> skillResponse = recordService.saveSkill(requestMember,
                recordService.findByMemberId(requestMember.getId()).getId(), skillReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "스킬 생성 완료", skillResponse));
    }

    @PatchMapping("/skill")
    @Operation(summary = "스킬 수정")
    public ResponseEntity<Object> patchSkill(@RequestHeader("Authorization") String token,
                                             Long skillId,
                                             @Valid @RequestBody SkillReqDto skillReqDto) {
        Member requestMember = loginUser.extractMemberId(token);
        List<SkillResponse> skillResponse = recordService.updateSkill(requestMember, skillId, skillReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "스킬 수정 완료", skillResponse));
    }

    @DeleteMapping("/skill")
    @Operation(summary = "스킬 삭제")
    public ResponseEntity<Object> deleteSkill(@RequestHeader("Authorization") String token,
                                              Long skillId) {
        Member requestMember = loginUser.extractMemberId(token);
        List<SkillResponse> skillResponse = recordService.deleteSkill(requestMember, skillId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "스킬 삭제 완료", skillResponse));
    }
}
