package umc.kkijuk.server.record.service;

import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.record.controller.response.*;
import umc.kkijuk.server.record.domain.Record;
import umc.kkijuk.server.record.dto.*;

import java.util.List;
import java.util.Optional;

public interface RecordService {
    Record findByMemberId(Long memberId);
    RecordResponse saveRecord(Member requestMember, RecordReqDto recordReqDto);
    List<EducationResponse> saveEducation(Member requestMember, Long recordId, EducationReqDto educationReqDto);
    List<EducationResponse> deleteEducation(Member requestMember, Long educationId);
    List<EducationResponse> updateEducation(Member requestMember, Long educationId, EducationReqDto educationReqDto);
    RecordResponse getRecord(Long memberId);
    RecordResponse updateRecord(Long memberId, Long recordId, RecordReqDto recordReqDto);
    LicenseResponse saveLicense(Member requestMember, Long recordId, LicenseReqDto licenseReqDto);
    LicenseResponse updateLicense(Member requestMember, Long licenseId, LicenseReqDto licenseReqDto);
    Long deleteLicense(Member requestMember, Long licenseId);
    AwardResponse saveAward(Member requestMember, Long recordId, AwardReqDto awardReqDto);
    AwardResponse updateAward(Member requestMember, Long awardId, AwardReqDto awardReqDto);
    Long deleteAward(Member requestMember, Long awardId);
    SkillResponse saveSkill(Member requestMember, Long recordId, SkillReqDto skillReqDto);
    SkillResponse updateSkill(Member requestMember, Long skillId, SkillReqDto skillReqDto);
    Long deleteSkill(Member requestMember, Long skillId);
    RecordDownResponse downloadResume(Long recordId, Long memberId);
}
