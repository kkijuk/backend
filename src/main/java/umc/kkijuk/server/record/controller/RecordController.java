package umc.kkijuk.server.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "record", description = "이력서 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/history/resume")
public class RecordController {
    private final RecordService recordService;
    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "이력서 생성")
    public ResponseEntity<Object> save(@RequestBody RecordReqDto recordReqDto) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        RecordResponse recordResponse = recordService.saveRecord(requestMember, recordReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "이력서 생성 완료", recordResponse));
    }

    @GetMapping
    @Operation(summary = "이력서 전체 조회")
    public ResponseEntity<Object> get() {
        Long loginUser = LoginUser.get().getId();
        RecordResponse recordResponse = recordService.getRecord(loginUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "이력서 전체 조회 완료", recordResponse));
    }

    @PatchMapping
    @Operation(summary = "이력서 정보 수정")
    public ResponseEntity<Object> update(@RequestBody RecordReqDto recordReqDto) {
        Long loginUser = LoginUser.get().getId();
        RecordResponse recordResponse = recordService.updateRecord(loginUser,
                recordService.findByMemberId(loginUser).getId(), recordReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "이력서 수정 완료", recordResponse));
    }

    @GetMapping("/download")
    @Operation(summary = "이력서 내보내기", description = "이력서 내보내기에 필요한 정보들을 조회합니다.")
    public ResponseEntity<Object> downloadResume() {
        Long loginUser = LoginUser.get().getId();
        RecordDownResponse response = recordService.downloadResume(recordService.findByMemberId(loginUser).getId(), loginUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "이력서 내보내기 정보 조회 완료", response));
    }

    @PostMapping("/education")
    @Operation(summary = "학력 생성")
    public ResponseEntity<Object> saveEducation(@RequestBody EducationReqDto educationReqDto) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        EducationResponse educationResponse = recordService.saveEducation(requestMember,
                recordService.findByMemberId(loginUser).getId(), educationReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "학력 생성 완료", educationResponse));
    }

    @PatchMapping("/education")
    @Operation(summary = "학력 수정")
    public ResponseEntity<Object> patchEducation(Long educationId, @RequestBody EducationReqDto educationReqDto) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        EducationResponse educationResponse = recordService.updateEducation(requestMember, educationId, educationReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "학력 수정 완료", educationResponse));
    }

    @DeleteMapping("/education")
    @Operation(summary = "학력 삭제")
    public ResponseEntity<Object> deleteEducation(Long educationId) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        Long id = recordService.deleteEducation(requestMember, educationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "학력 삭제 완료", "id: " + id));
    }

    @PostMapping("/license")
    @Operation(summary = "자격증 생성")
    public ResponseEntity<Object> saveLicense(@RequestBody LicenseReqDto licenseReqDto) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        LicenseResponse licenseResponse = recordService.saveLicense(requestMember,
                recordService.findByMemberId(loginUser).getId(), licenseReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자격증 생성 완료", licenseResponse));
    }

    @PatchMapping("/license")
    @Operation(summary = "자격증 수정")
    public ResponseEntity<Object> patchLicense(Long licenseId, @RequestBody LicenseReqDto licenseReqDto) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        LicenseResponse licenseResponse = recordService.updateLicense(requestMember, licenseId, licenseReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자격증 수정 완료", licenseResponse));
    }

    @DeleteMapping("/license")
    @Operation(summary = "자격증 삭제")
    public ResponseEntity<Object> deleteLicense(Long licenseId) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        Long id = recordService.deleteLicense(requestMember, licenseId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자격증 삭제 완료", "id: " + id));
    }

    @PostMapping("/award")
    @Operation(summary = "수상 생성")
    public ResponseEntity<Object> saveAward(@RequestBody AwardReqDto awardReqDto) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        AwardResponse awardResponse = recordService.saveAward(requestMember,
                recordService.findByMemberId(loginUser).getId(), awardReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "수상 생성 완료", awardResponse));
    }

    @PatchMapping("/award")
    @Operation(summary = "수상 수정")
    public ResponseEntity<Object> patchAward(Long awardId, @RequestBody AwardReqDto awardReqDto) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        AwardResponse awardResponse = recordService.updateAward(requestMember, awardId, awardReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "수상 수정 완료", awardResponse));
    }

    @DeleteMapping("/award")
    @Operation(summary = "수상 삭제")
    public ResponseEntity<Object> deleteAward(Long awardId) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        Long id = recordService.deleteAward(requestMember, awardId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "수상 삭제 완료", "id: " + id));
    }

    @PostMapping("/skill")
    @Operation(summary = "스킬 생성")
    public ResponseEntity<Object> saveSkill(@RequestBody SkillReqDto skillReqDto) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        SkillResponse skillResponse = recordService.saveSkill(requestMember,
                recordService.findByMemberId(loginUser).getId(), skillReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "스킬 생성 완료", skillResponse));
    }

    @PatchMapping("/skill")
    @Operation(summary = "스킬 수정")
    public ResponseEntity<Object> patchSkill(Long skillId, @RequestBody SkillReqDto skillReqDto) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        SkillResponse skillResponse = recordService.updateSkill(requestMember, skillId, skillReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "스킬 수정 완료", skillResponse));
    }

    @DeleteMapping("/skill")
    @Operation(summary = "스킬 삭제")
    public ResponseEntity<Object> deleteSkill(Long skillId) {
        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        Long id = recordService.deleteSkill(requestMember, skillId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "스킬 삭제 완료", "id: " + id));
    }
}
