package umc.kkijuk.server.career.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.career.controller.response.*;
import umc.kkijuk.server.career.domain.*;
import umc.kkijuk.server.career.dto.*;
import umc.kkijuk.server.career.dto.converter.BaseCareerConverter;
import umc.kkijuk.server.career.repository.*;
import umc.kkijuk.server.common.domian.exception.OwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.detail.domain.BaseCareerDetail;
import umc.kkijuk.server.detail.domain.CareerType;
import umc.kkijuk.server.detail.repository.CareerDetailRepository;
import umc.kkijuk.server.member.domain.Member;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;

@Service
@Builder
@RequiredArgsConstructor
public class CareerServiceImpl implements CareerService{
    private final ActivityRepository activityRepository;
    private final CircleRepository circleRepository;
    private final CompetitionRepository competitionRepository;
    private final EduCareerRepository eduCareerRepository;
    private final ProjectRepository projectJpaRepository;
    private final EmploymentRepository employmentRepository;
    private final CareerDetailRepository detailRepository;
    private final CareerEtcRepository etcRepository;

    @Override
    @Transactional
    public ActivityResponse createActivity(Member requestMember, ActivityReqDto activityReqDto) {
        Activity activity = BaseCareerConverter.toAvtivity(requestMember, activityReqDto);
        setCommonFields(activity);
        return new ActivityResponse(activityRepository.save(activity));

    }
    @Override
    @Transactional
    public CircleResponse createCircle(Member requestMember, CircleReqDto circleReqDto) {
        Circle circle = BaseCareerConverter.toCircle(requestMember, circleReqDto);
        setCommonFields(circle);
        return new CircleResponse(circleRepository.save(circle));
    }

    @Override
    @Transactional
    public CompetitionResponse createCompetition(Member requestMember, CompetitionReqDto competitionReqDto) {
        Competition competition = BaseCareerConverter.toCompetition(requestMember, competitionReqDto);
        setCommonFields(competition);
        return new CompetitionResponse(competitionRepository.save(competition));

    }

    @Override
    @Transactional
    public EduCareerResponse crateEduCareer(Member requestMember, EduCareerReqDto eduCareerReqDto) {
        EduCareer edu = BaseCareerConverter.toEduCareer(requestMember,eduCareerReqDto);
        setCommonFields(edu);
        return new EduCareerResponse(eduCareerRepository.save(edu));
    }

    @Override
    @Transactional
    public EmploymentResponse createEmployment(Member requestMember, EmploymentReqDto employmentReqDto) {
        Employment emp = BaseCareerConverter.toEmployment(requestMember, employmentReqDto);
        setCommonFields(emp);
        return new EmploymentResponse(employmentRepository.save(emp));
    }

    @Override
    @Transactional
    public ProjectResponse createProject(Member requestMember, ProjectReqDto projectReqDto) {
        Project project = BaseCareerConverter.toProject(requestMember, projectReqDto);
        setCommonFields(project);
        return new ProjectResponse(projectJpaRepository.save(project));
    }
    @Override
    @Transactional
    public EtcResponse createEtc(Member requestMember, EtcReqDto etcReqDto){
        CareerEtc etc = BaseCareerConverter.toEtc(requestMember, etcReqDto);
        setCommonFields(etc);
        return new EtcResponse(etcRepository.save(etc));
    }

    @Override
    @Transactional
    public void deleteBaseCareer(Member requestMember, Long careerId, String type) {
        switch (type.toLowerCase()) {
            case "activity" -> deleteActivity(requestMember, careerId);
            case "circle" -> deleteCircle(requestMember, careerId);
            case "project" -> deleteProject(requestMember, careerId);
            case "edu" -> deleteEdu(requestMember, careerId);
            case "competition" -> deleteComp(requestMember, careerId);
            case "employment" -> deleteEmp(requestMember, careerId);
            case "etc" -> deleteEtc(requestMember, careerId);
            default -> throw new IllegalArgumentException("지원하지 않는 활동 유형입니다 : " + type);
        }
    }

    @Override
    @Transactional
    public void deleteActivity(Member requestMember, Long activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(
                () -> new ResourceNotFoundException("Activity",activityId)
        );
        if(!activity.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        activityRepository.delete(activity);
    }

    @Override
    @Transactional
    public void deleteCircle(Member requestMember, Long circleId) {
        Circle circle = circleRepository.findById(circleId).orElseThrow(
                () -> new ResourceNotFoundException("Circle",circleId)
        );
        if(!circle.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        circleRepository.delete(circle);
    }

    @Override
    @Transactional
    public void deleteComp(Member requestMember, Long competitionId) {
        Competition comp = competitionRepository.findById(competitionId).orElseThrow(
                () -> new ResourceNotFoundException("Competition",competitionId)
        );
        if(!comp.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        competitionRepository.delete(comp);
    }

    @Override
    @Transactional
    public void deleteEdu(Member requestMember, Long educareerId) {
        EduCareer eduCareer = eduCareerRepository.findById(educareerId).orElseThrow(
                () -> new ResourceNotFoundException("EduCareer",educareerId)
        );
        if(!eduCareer.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        eduCareerRepository.delete(eduCareer);
    }

    @Override
    @Transactional
    public void deleteEmp(Member requestMember, Long employmentId) {
        Employment employment = employmentRepository.findById(employmentId).orElseThrow(
                () -> new ResourceNotFoundException("Employment",employmentId)
        );
        if(!employment.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        employmentRepository.delete(employment);
    }

    @Override
    @Transactional
    public void deleteProject(Member requestMember, Long projectId) {
        Project project = projectJpaRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project",projectId)
        );
        if(!project.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        projectJpaRepository.delete(project);
    }
    @Override
    @Transactional
    public void deleteEtc(Member requestMember, Long etcId){
        CareerEtc etc = etcRepository.findById(etcId).orElseThrow(
                () -> new ResourceNotFoundException("Etc", etcId)
        );
        if(!etc.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        etcRepository.delete(etc);
    }
    @Override
    @Transactional
    public ActivityResponse updateActivity(Member requestMember, Long activityId, ActivityReqDto request){
        Activity updateActivity = activityRepository.findById(activityId).orElseThrow(
                () -> new ResourceNotFoundException("Activity",activityId)
        );
        if(!updateActivity.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        updateActivity.updateActivity(
                request.getName(),
                request.getAlias(),
                request.getUnknown(),
                request.getStartdate(),
                request.getEnddate(),
                request.getOrganizer(),
                request.getRole(),
                request.getTeamSize(),
                request.getContribution(),
                request.getIsTeam()
        );
        return new ActivityResponse(updateActivity);
    }

    @Override
    @Transactional
    public CircleResponse updateCircle(Member requestMember, Long circleId, CircleReqDto request) {
        Circle updateCircle = circleRepository.findById(circleId).orElseThrow(
                () -> new ResourceNotFoundException("Circle",circleId)
        );
        if(!updateCircle.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        updateCircle.updateCircle(
                request.getName(),
                request.getAlias(),
                request.getUnknown(),
                request.getStartdate(),
                request.getEnddate(),
                request.getLocation(),
                request.getRole()
        );
        return new CircleResponse(updateCircle);
    }

    @Override
    @Transactional
    public CompetitionResponse updateComp(Member requestMember, Long competitionId, CompetitionReqDto request) {
        Competition updateComp = competitionRepository.findById(competitionId).orElseThrow(
                () -> new ResourceNotFoundException("Competition",competitionId)
        );
        if(!updateComp.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        updateComp.updateComp(
                request.getName(),
                request.getAlias(),
                request.getUnknown(),
                request.getStartdate(),
                request.getEnddate(),
                request.getOrganizer(),
                request.getTeamSize(),
                request.getContribution(),
                request.getIsTeam()

        );
        return new CompetitionResponse(updateComp);
    }

    @Override
    @Transactional
    public EduCareerResponse updateEdu(Member requestMember, Long educareerId, EduCareerReqDto request) {
        EduCareer updateEduCareer = eduCareerRepository.findById(educareerId).orElseThrow(
                () -> new ResourceNotFoundException("EduCareer",educareerId)
        );
        if(!updateEduCareer.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        updateEduCareer.updateEduCareer(
                request.getName(),
                request.getAlias(),
                request.getUnknown(),
                request.getStartdate(),
                request.getEnddate(),
                request.getOrganizer(),
                request.getTime()
        );
        return new EduCareerResponse(updateEduCareer);
    }

    @Override
    @Transactional
    public EmploymentResponse updateEmp(Member requestMember, Long employmentId, EmploymentReqDto request) {
        Employment updateEmployment = employmentRepository.findById(employmentId).orElseThrow(
                () -> new ResourceNotFoundException("Employment",employmentId)
        );
        if(!updateEmployment.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        updateEmployment.updateEmployment(
                request.getName(),
                request.getAlias(),
                request.getUnknown(),
                request.getStartdate(),
                request.getEnddate(),
                request.getType(),
                request.getPosition(),
                request.getField()
        );
        return new EmploymentResponse(updateEmployment);

    }

    @Override
    @Transactional
    public EtcResponse updateEtc(Member requestMember, Long etcId, EtcReqDto request) {
        CareerEtc updateEtc = etcRepository.findById(etcId).orElseThrow(
                () -> new ResourceNotFoundException("Etc",etcId)
        );
        if(!updateEtc.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        updateEtc.updateCareerEtc(
                request.getName(),
                request.getAlias(),
                request.getUnknown(),
                request.getStartdate(),
                request.getEnddate()
        );
        return new EtcResponse(updateEtc);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Member requestMember, Long projectId, ProjectReqDto request) {
        Project updateProject = projectJpaRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project",projectId)
        );
        if(!updateProject.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }
        updateProject.updateProject(
                request.getName(),
                request.getAlias(),
                request.getUnknown(),
                request.getStartdate(),
                request.getEnddate(),
                request.getTeamSize(),
                request.getIsTeam(),
                request.getContribution(),
                request.getLocation()

        );
        return new ProjectResponse(updateProject);
    }

    @Override
    @Transactional
    public BaseCareerResponse createSummary(Member requestMember, Long careerId, CareerSummaryReqDto request) {
        switch (request.getType()){
            case ACTIVITY -> {
                Activity activity = activityRepository.findById(careerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Activity",careerId));
                if(!activity.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                activity.setSummary(request.getSummary());
                return getResponse(activity, ActivityResponse::new);
            }
            case PROJECT -> {
                Project project = projectJpaRepository.findById(careerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Project", careerId));
                if(!project.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                project.setSummary(request.getSummary());
                return getResponse(project, ProjectResponse::new);

            }
            case CIRCLE -> {
                Circle circle = circleRepository.findById(careerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Circle", careerId));
                if(!circle.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                circle.setSummary(request.getSummary());
                return getResponse(circle, CircleResponse::new);
            }
            case COM -> {
                Competition competition = competitionRepository.findById(careerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Competition",careerId));
                if(!competition.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                competition.setSummary(request.getSummary());
                return getResponse(competition, CompetitionResponse::new);
            }
            case EDU -> {
                EduCareer eduCareer = eduCareerRepository.findById(careerId)
                        .orElseThrow(() -> new ResourceNotFoundException("EduCareer",careerId));
                if(!eduCareer.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                eduCareer.setSummary(request.getSummary());
                return getResponse(eduCareer, EduCareerResponse::new);
            }
            case EMP -> {
                Employment employment = employmentRepository.findById(careerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Employment",careerId));
                if(!employment.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                employment.setSummary(request.getSummary());
                return getResponse(employment,EmploymentResponse::new);
            }
            default -> throw new IllegalArgumentException("지원하지 않는 활동 유형입니다 : " + request.getType());

        }
    }

    private <T extends BaseCareer, R extends BaseCareerResponse> R getResponse(T career,  BiFunction<T, List<BaseCareerDetail>, R> responseConstructor) {
        List<BaseCareerDetail> details;
        if (career instanceof Activity) {
            details = Optional.ofNullable(detailRepository.findByCareerIdAndCareerType(CareerType.ACTIVITY, career.getId()))
                    .orElseGet(Collections::emptyList);
        } else if (career instanceof Circle) {
            details = Optional.ofNullable(detailRepository.findByCareerIdAndCareerType(CareerType.CIRCLE, career.getId()))
                    .orElseGet(Collections::emptyList);
        } else if (career instanceof Competition) {
            details = Optional.ofNullable(detailRepository.findByCareerIdAndCareerType(CareerType.COM, career.getId()))
                    .orElseGet(Collections::emptyList);
        } else if (career instanceof EduCareer) {
            details =Optional.ofNullable(detailRepository.findByCareerIdAndCareerType(CareerType.EDU, career.getId()))
                    .orElseGet(Collections::emptyList);
        } else if (career instanceof Employment) {
            details = Optional.ofNullable( detailRepository.findByCareerIdAndCareerType(CareerType.EMP, career.getId()))
                    .orElseGet(Collections::emptyList);
        } else if (career instanceof Project) {
            details = Optional.ofNullable( detailRepository.findByCareerIdAndCareerType(CareerType.PROJECT, career.getId()))
                    .orElseGet(Collections::emptyList);
        } else if (career instanceof CareerEtc) {
            details = Optional.ofNullable( detailRepository.findByCareerIdAndCareerType(CareerType.ETC, career.getId()))
                    .orElseGet(Collections::emptyList);
        }
        else {
            throw new IllegalArgumentException("지원하지 않는 타입입니다.");
        }
        return responseConstructor.apply(career,details);
    }

    private <T extends BaseCareer> void setCommonFields(T activity) {
        if (activity.getUnknown()) {
            activity.setEnddate(LocalDate.now());
            activity.setYear(LocalDate.now().getYear());
        } else {
            activity.setYear(activity.getEnddate().getYear());
        }
    }
}
