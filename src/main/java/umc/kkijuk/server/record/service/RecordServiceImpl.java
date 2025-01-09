package umc.kkijuk.server.record.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.career.controller.response.*;
import umc.kkijuk.server.career.repository.*;
import umc.kkijuk.server.common.domian.exception.IntroFoundException;
import umc.kkijuk.server.common.domian.exception.IntroOwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.RecordNotFoundException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.detail.domain.CareerType;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.repository.MemberRepository;
import umc.kkijuk.server.record.controller.response.*;
import umc.kkijuk.server.record.domain.*;
import umc.kkijuk.server.record.domain.Record;
import umc.kkijuk.server.record.repository.*;
import umc.kkijuk.server.record.dto.*;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final MemberRepository memberRepository;
    private final EducationRepository educationRepository;
    private final LicenseRepository licenseRepository;
    private final AwardRepository awardRepository;
    private final SkillRepository skillRepository;

    private final ActivityRepository activityRepository;
    private final CircleRepository circleRepository;
    private final CompetitionRepository competitionJpaRepository;
    private final EduCareerRepository eduCareerJpaRepository;
    private final EmploymentRepository employmentJpaRepository;
    private final ProjectRepository projectJpaRepository;
    private final CareerEtcRepository etcRepository;

    private final FileRepository fileRepository;

    @Override
    public Record findByMemberId(Long memberId) {
        if (!recordRepository.existsByMemberId(memberId)) {
            throw new ResourceNotFoundException("Record", memberId);
        }
        return recordRepository.findByMemberId(memberId);
    }
    @Override
    @Transactional
    public RecordResponse saveRecord(Member requestMember, RecordReqDto recordReqDto) {
        if (recordRepository.existsByMemberId(requestMember.getId())) {
            throw new IntroFoundException("이미 이력서가 존재합니다");
        }

        Record record = Record.builder()
                .memberId(requestMember.getId())
                .address(recordReqDto.getAddress())
                .profileImageUrl(recordReqDto.getProfileImageUrl())
                .build();

        recordRepository.save(record);

        return new RecordResponse(record, requestMember, null, null, null,null,null, null, null, null, null);
    }

    @Override
    @Transactional
    public RecordResponse getRecord(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("member ", memberId));

        Record record = recordRepository.findByMemberId(memberId);

        if (record == null) {
            throw new RecordNotFoundException();
        }

        // 경력
        List<EmploymentResponse> employments = employmentJpaRepository.findByMemberId(memberId).stream()
                .map(EmploymentResponse::new)
                .sorted(Comparator.comparing(EmploymentResponse::getEndDate).reversed())
                .toList();

        // 활동 및 경험 ( 동아리, 대외활동, 기타 )
        List<BaseCareerResponse> activitiesAndExperiences = activityRepository.findByMemberId(memberId).stream()
                .map(ActivityResponse::new)
                .collect(Collectors.toList());

        activitiesAndExperiences.addAll(circleRepository.findByMemberId(memberId).stream()
                .map(CircleResponse::new).collect(Collectors.toList()));
        activitiesAndExperiences.addAll(etcRepository.findByMemberId(memberId).stream()
                .map(EtcResponse::new).collect(Collectors.toList()));

        activitiesAndExperiences.stream().sorted(Comparator.comparing(BaseCareerResponse::getEndDate).reversed());

        // 프로젝트 ( 프로젝트, 공모전/대회)
        List<BaseCareerResponse> projectsAndComp = projectJpaRepository.findByMemberId(memberId).stream()
                .map(ProjectResponse::new)
                .collect(Collectors.toList());
        projectsAndComp.addAll(competitionJpaRepository.findByMemberId(memberId).stream()
                .map(CompetitionResponse::new).collect(Collectors.toList()));
        projectsAndComp.stream().sorted(Comparator.comparing(BaseCareerResponse::getEndDate).reversed());

        // 교육 ( 교육)
        List<EduCareerResponse> eduCareers = eduCareerJpaRepository.findByMemberId(memberId).stream()
                .map(EduCareerResponse::new)
                .sorted(Comparator.comparing(EduCareerResponse::getEndDate).reversed())
                .toList();

        // 수상
        List<AwardResponse> awards = awardRepository.findByMemberId(memberId).stream()
                .map(AwardResponse::new)
                .sorted(Comparator.comparing(AwardResponse::getAcquireDate).reversed())
                .toList();

        // 자격증
        List<LicenseResponse> licenses = licenseRepository.findByMemberId(memberId).stream()
                .map(LicenseResponse::new)
                .sorted(Comparator.comparing(LicenseResponse::getAcquireDate).reversed())
                .toList();

        // 스킬
        List<SkillResponse> skills = skillRepository.findByMemberId(memberId).stream()
                .map(SkillResponse::new)
                .collect(Collectors.toList());

        // 파일
        List<FileResponse> files = fileRepository.findByMemberId(memberId).stream()
                .map(FileResponse::new)
                .collect(Collectors.toList());

        // 학력
        List<EducationResponse> educationList = educationRepository.findByMemberId(memberId)
                .stream()
                .map(EducationResponse::new)
                .collect(Collectors.toList());

        return new RecordResponse(record, member, educationList, employments,
                activitiesAndExperiences, projectsAndComp, eduCareers, awards, licenses, skills, files);
    }


    @Override
    @Transactional
    public RecordResponse updateRecord(Long memberId, Long recordId, RecordReqDto recordReqDto) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("record ", recordId));
        if (!record.getMemberId().equals(memberId)) {
            throw new IntroOwnerMismatchException();
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("member ", memberId));

        //경력
        List<EmploymentResponse> employments = employmentJpaRepository.findByMemberId(memberId).stream()
                .map(EmploymentResponse::new)
                .sorted(Comparator.comparing(EmploymentResponse::getEndDate).reversed())
                .toList();

        //활동 및 경험 ( 동아리, 대외활동, 기타 )
        List<BaseCareerResponse> activitiesAndExperiences = activityRepository.findByMemberId(memberId).stream()
                .map(ActivityResponse::new)
                .collect(Collectors.toList());
        activitiesAndExperiences.addAll(circleRepository.findByMemberId(memberId).stream()
                .map(CircleResponse::new).collect(Collectors.toList()));
        activitiesAndExperiences.addAll(etcRepository.findByMemberId(memberId).stream()
                .map(EtcResponse::new).collect(Collectors.toList()));

        activitiesAndExperiences.stream().sorted(Comparator.comparing(BaseCareerResponse::getEndDate).reversed());

        //프로젝트 ( 프로젝트, 공모전/대회)
        List<BaseCareerResponse> projectsAndComp = projectJpaRepository.findByMemberId(memberId).stream()
                .map(ProjectResponse::new)
                .collect(Collectors.toList());
        projectsAndComp.addAll(competitionJpaRepository.findByMemberId(memberId).stream()
                .map(CompetitionResponse::new).collect(Collectors.toList()));
        projectsAndComp.stream().sorted(Comparator.comparing(BaseCareerResponse::getEndDate).reversed());

        //교육 ( 교육)
        List<EduCareerResponse> eduCareers = eduCareerJpaRepository.findByMemberId(memberId).stream()
                .map(EduCareerResponse::new)
                .sorted(Comparator.comparing(EduCareerResponse::getEndDate).reversed())
                .toList();

        // 수상
        List<AwardResponse> awards = awardRepository.findByMemberId(memberId).stream()
                .map(AwardResponse::new)
                .sorted(Comparator.comparing(AwardResponse::getAcquireDate).reversed())
                .toList();

        // 자격증
        List<LicenseResponse> licenses = licenseRepository.findByMemberId(memberId).stream()
                .map(LicenseResponse::new)
                .sorted(Comparator.comparing(LicenseResponse::getAcquireDate).reversed())
                .toList();

        // 스킬
        List<SkillResponse> skills = skillRepository.findByMemberId(memberId).stream()
                .map(SkillResponse::new)
                .collect(Collectors.toList());

        // 파일
        List<FileResponse> files = fileRepository.findByMemberId(memberId).stream()
                .map(FileResponse::new)
                .collect(Collectors.toList());

        record.update(
                recordReqDto.getAddress(),
                recordReqDto.getProfileImageUrl());

        //학력
        List<EducationResponse> educationList = educationRepository.findByMemberId(memberId)
                .stream()
                .map(EducationResponse::new)
                .collect(Collectors.toList());

        return new RecordResponse(record, member, educationList, employments,
                activitiesAndExperiences, projectsAndComp,eduCareers , awards, licenses, skills, files);
    }

    @Override
    public RecordDownResponse downloadResume(Long recordId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("member ", memberId));
        Record record = recordRepository.findByMemberId(memberId);

        List<ResumeResponse> employments = employmentJpaRepository.findByMemberId(memberId).stream()
                .map(employment -> new ResumeResponse(employment.getId(),CareerType.EMP.getDescription(),employment.getName(),
                        employment.getAlias(),employment.getSummary(),employment.getStartdate(),
                        employment.getEnddate())).collect(Collectors.toList());
        //활동 및 경험 ( 대외 활동, 동아리, 기타 )

        List<ResumeResponse> activitiesAndExperiences = activityRepository.findByMemberId(memberId).stream()
                .map(activity -> new ResumeResponse(activity.getId(), CareerType.ACTIVITY.getDescription(), activity.getName(),
                        activity.getAlias(),activity.getSummary(),activity.getStartdate(),
                        activity.getEnddate())).collect(Collectors.toList());

        activitiesAndExperiences.addAll(circleRepository.findByMemberId(memberId).stream()
                .map(circle->new ResumeResponse(circle.getId(),CareerType.CIRCLE.getDescription(), circle.getName(),
                        circle.getAlias(),circle.getSummary(),circle.getStartdate(),
                        circle.getEnddate())).collect(Collectors.toList()));

        activitiesAndExperiences.addAll(etcRepository.findByMemberId(memberId).stream()
                .map(etc -> new ResumeResponse(etc.getId(), CareerType.ETC.getDescription(), etc.getName(),
                        etc.getAlias(), etc.getSummary(), etc.getStartdate(),
                        etc.getEnddate())).collect(Collectors.toList()));

        //프로젝트 ( 프로젝트, 공모전/대회)
        List<ResumeResponse> projectsAndComp = projectJpaRepository.findByMemberId(memberId).stream()
                .map(project->new ResumeResponse(project.getId(),CareerType.PROJECT.getDescription(), project.getName(),
                        project.getAlias(),project.getSummary(),project.getStartdate(),
                        project.getEnddate())).collect(Collectors.toList());

        projectsAndComp.addAll(competitionJpaRepository.findByMemberId(memberId).stream()
                .map(comp -> new ResumeResponse(comp.getId(),CareerType.COM.getDescription(), comp.getName(),
                        comp.getAlias(),comp.getSummary(),comp.getStartdate(),
                        comp.getEnddate())).collect(Collectors.toList()));

        List<ResumeResponse> eduCareers = eduCareerJpaRepository.findByMemberId(memberId).stream()
                .map(edu-> new ResumeResponse(edu.getId(),CareerType.EDU.getDescription(),edu.getName(),
                        edu.getAlias(),edu.getSummary(),edu.getStartdate(),
                        edu.getEnddate())).collect(Collectors.toList());

        // 학력
        List<EducationResponse> educationList = educationRepository.findByMemberId(memberId)
                .stream()
                .map(EducationResponse::new)
                .collect(Collectors.toList());

        // 수상
        List<AwardResponse> awards = awardRepository.findByMemberId(memberId).stream()
                .map(AwardResponse::new)
                .sorted(Comparator.comparing(AwardResponse::getAcquireDate).reversed())
                .toList();

        // 자격증
        List<LicenseResponse> licenses = licenseRepository.findByMemberId(memberId).stream()
                .map(LicenseResponse::new)
                .sorted(Comparator.comparing(LicenseResponse::getAcquireDate).reversed())
                .toList();

        // 스킬
        List<SkillResponse> skills = skillRepository.findByMemberId(memberId).stream()
                .map(SkillResponse::new)
                .collect(Collectors.toList());

        // 파일
        List<FileResponse> files = fileRepository.findByMemberId(memberId).stream()
                .map(FileResponse::new)
                .collect(Collectors.toList());


        return new RecordDownResponse(record, member, educationList, employments,
                activitiesAndExperiences, projectsAndComp, eduCareers, awards, licenses, skills, files);
    }

    @Override
    @Transactional
    public EducationResponse saveEducation(Member requestMember, Long recordId, EducationReqDto educationReqDto) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Record", recordId));
        if (!record.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        Education education = Education.builder()
                .memberId(requestMember.getId())
                .category(educationReqDto.getCategory())
                .schoolName(educationReqDto.getSchoolName())
                .major(educationReqDto.getMajor())
                .state(educationReqDto.getState())
                .admissionDate(educationReqDto.getAdmissionDate())
                .graduationDate(educationReqDto.getGraduationDate())
                .build();

        educationRepository.save(education);

        return new EducationResponse(education);
    }

    @Override
    @Transactional
    public EducationResponse updateEducation(Member requestMember, Long educationId, EducationReqDto educationReqDto) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("education ", educationId));
        if (!education.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }
        education.changeEducationInfo(
                educationReqDto.getCategory(),
                educationReqDto.getSchoolName(),
                educationReqDto.getMajor(),
                educationReqDto.getState(),
                educationReqDto.getAdmissionDate(),
                educationReqDto.getGraduationDate());

        return new EducationResponse(education);
    }
    @Override
    @Transactional
    public Long deleteEducation(Member requestMember, Long educationId) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("education ", educationId));
        if (!education.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        educationRepository.delete(education);

        return education.getId();
    }

    @Override
    @Transactional
    public LicenseResponse saveLicense(Member requestMember, Long recordId, LicenseReqDto licenseReqDto) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Record", recordId));

        if (!record.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        License license = License.builder()
                .memberId(requestMember.getId())
                .licenseTag((licenseReqDto.getLicenseTag()))
                .licenseName(licenseReqDto.getLicenseName())
                .administer(licenseReqDto.getAdminister())
                .licenseNumber(licenseReqDto.getLicenseNumber())
                .licenseGrade(licenseReqDto.getLicenseGrade())
                .acquireDate(licenseReqDto.getAcquireDate())
                .build();

        licenseRepository.save(license);

        return new LicenseResponse(license);
    }

    @Override
    @Transactional
    public LicenseResponse updateLicense(Member requestMember, Long licenseId, LicenseReqDto licenseReqDto) {

        License license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new ResourceNotFoundException("License", licenseId));

        if (!license.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        license.changeLicenseInfo(
                licenseReqDto.getLicenseTag(),
                licenseReqDto.getLicenseName(),
                licenseReqDto.getAdminister(),
                licenseReqDto.getLicenseNumber(),
                licenseReqDto.getLicenseGrade(),
                licenseReqDto.getAcquireDate());

        return new LicenseResponse(license);
    }

    @Override
    @Transactional
    public Long deleteLicense(Member requestMember, Long licenseId) {

        License license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new ResourceNotFoundException("License", licenseId));

        if (!license.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        licenseRepository.delete(license);

        return license.getId();
    }

    @Override
    @Transactional
    public AwardResponse saveAward(Member requestMember, Long recordId, AwardReqDto awardReqDto) {

        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Record", recordId));

        if (!record.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        Award award = Award.builder()
                .memberId(requestMember.getId())
                .competitionName(awardReqDto.getCompetitionName())
                .administer(awardReqDto.getAdminister())
                .awardName(awardReqDto.getAwardName())
                .acquireDate(awardReqDto.getAcquireDate())
                .build();

        awardRepository.save(award);

        return new AwardResponse(award);
    }

    @Override
    @Transactional
    public AwardResponse updateAward(Member requestMember, Long awardId, AwardReqDto awardReqDto) {

        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new ResourceNotFoundException("Award", awardId));

        if (!award.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        award.changeAwardInfo(
                awardReqDto.getCompetitionName(),
                awardReqDto.getAdminister(),
                awardReqDto.getAwardName(),
                awardReqDto.getAcquireDate()
        );

        return new AwardResponse(award);
    }

    @Override
    @Transactional
    public Long deleteAward(Member requestMember, Long awardId) {

        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new ResourceNotFoundException("Award", awardId));

        if (!award.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        awardRepository.delete(award);

        return award.getId();
    }

    @Override
    @Transactional
    public SkillResponse saveSkill(Member requestMember, Long recordId, SkillReqDto skillReqDto) {

        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Record", recordId));

        if (!record.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        Skill skill = Skill.builder()
                .memberId(requestMember.getId())
                .skillTag(skillReqDto.getSkillTag())
                .skillName(skillReqDto.getSkillName())
                .workmanship(skillReqDto.getWorkmanship())
                .build();

        skillRepository.save(skill);

        return new SkillResponse(skill);
    }

    @Override
    @Transactional
    public SkillResponse updateSkill(Member requestMember, Long skillId, SkillReqDto skillReqDto) {

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", skillId));

        if (!skill.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        skill.changeSkillInfo(
                skillReqDto.getSkillTag(),
                skillReqDto.getSkillName(),
                skillReqDto.getWorkmanship()
        );

        return new SkillResponse(skill);
    }

    @Override
    @Transactional
    public Long deleteSkill(Member requestMember, Long skillId) {

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", skillId));

        if (!skill.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        skillRepository.delete(skill);

        return skill.getId();
    }


}
