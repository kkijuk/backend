package umc.kkijuk.server.career.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.career.controller.response.*;
import umc.kkijuk.server.career.domain.*;
import umc.kkijuk.server.career.dto.converter.BaseCareerConverter;
import umc.kkijuk.server.career.repository.*;
import umc.kkijuk.server.common.domian.exception.OwnerMismatchException;
import umc.kkijuk.server.detail.controller.response.BaseCareerDetailResponse;
import umc.kkijuk.server.detail.domain.BaseCareerDetail;
import umc.kkijuk.server.detail.domain.CareerType;
import umc.kkijuk.server.detail.repository.CareerDetailRepository;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.tag.domain.Tag;
import umc.kkijuk.server.tag.repository.TagRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@Builder
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CareerSearchServiceImpl implements CareerSearchService{
    private final ActivityRepository activityRepository;
    private final CircleRepository circleRepository;
    private final CompetitionRepository competitionJpaRepository;
    private final EduCareerRepository eduCareerJpaRepository;
    private final ProjectRepository projectJpaRepository;
    private final EmploymentRepository employmentJpaRepository;
    private final CareerEtcRepository etcRepository;
    private final CareerDetailRepository detailRepository;
    private final TagRepository tagRepository;

    @Override
    public List<TimelineResponse> findCareerForTimeline(Member requestMember) {
        Long memberId = requestMember.getId();
        List<BaseCareer> careers = new ArrayList<>();


        careers.addAll(activityRepository.findByMemberId(memberId));
        careers.addAll(eduCareerJpaRepository.findByMemberId(memberId));
        careers.addAll(employmentJpaRepository.findByMemberId(memberId));
        careers.addAll(circleRepository.findByMemberId(memberId));
        careers.addAll(projectJpaRepository.findByMemberId(memberId));
        careers.addAll(competitionJpaRepository.findByMemberId(memberId));
        careers.addAll(etcRepository.findByMemberId(memberId));


        careers.sort(Comparator.comparing(BaseCareer::getEnddate).reversed());

        return careers.stream()
                .map(career -> new TimelineResponse(career, resolveCareerType(career)))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<?>> findAllCareerGroupedCategory(Long memberId) {
        List<EduCareerResponse> eduCareers = eduCareerJpaRepository.findByMemberId(memberId)
                .stream().map(EduCareerResponse::new).collect(Collectors.toList());
        List<EmploymentResponse> employments = employmentJpaRepository.findByMemberId(memberId)
                .stream().map(EmploymentResponse::new).collect(Collectors.toList());
        List<ProjectResponse> projects = projectJpaRepository.findByMemberId(memberId)
                .stream().map(ProjectResponse::new).collect(Collectors.toList());
        List<ActivityResponse> activities = activityRepository.findByMemberId(memberId)
                .stream().map(ActivityResponse::new).collect(Collectors.toList());
        List<CircleResponse> circles = circleRepository.findByMemberId(memberId)
                .stream().map(CircleResponse::new).collect(Collectors.toList());
        List<CompetitionResponse> competitions = competitionJpaRepository.findByMemberId(memberId)
                .stream().map(CompetitionResponse::new).collect(Collectors.toList());
        List<EtcResponse> etcs = etcRepository.findByMemberId(memberId)
                .stream().map(EtcResponse::new).collect(Collectors.toList());


        Map<String, List<?>> careerList = new HashMap<>();
        careerList.put("대외활동",activities);
        careerList.put("동아리",circles);
        careerList.put("대회",competitions);
        careerList.put("교육",eduCareers);
        careerList.put("인턴",employments);
        careerList.put("프로젝트",projects);
        careerList.put("기타", etcs);

        return careerList;
    }

    @Override
    public Map<String, List<?>> findAllCareerGroupedYear(Long memberId) {
        List<BaseCareerResponse> baseCareers = projectJpaRepository.findByMemberId(memberId).stream()
                .map(ProjectResponse::new)
                .collect(Collectors.toList());
        baseCareers.addAll(competitionJpaRepository.findByMemberId(memberId).stream()
                .map(CompetitionResponse::new).collect(Collectors.toList()));
        baseCareers.addAll(activityRepository.findByMemberId(memberId).stream()
                .map(ActivityResponse::new).collect(Collectors.toList()));
        baseCareers.addAll(circleRepository.findByMemberId(memberId).stream()
                .map(CircleResponse::new).collect(Collectors.toList()));
        baseCareers.addAll(eduCareerJpaRepository.findByMemberId(memberId).stream()
                .map(EduCareerResponse::new).collect(Collectors.toList()));
        baseCareers.addAll(employmentJpaRepository.findByMemberId(memberId).stream()
                .map(EmploymentResponse::new).collect(Collectors.toList()));
        baseCareers.addAll(etcRepository.findByMemberId(memberId).stream()
                .map(EtcResponse::new).collect(Collectors.toList()));


        baseCareers = baseCareers.stream()
                .sorted(Comparator.comparing(BaseCareerResponse::getEndDate).reversed())
                .collect(Collectors.toList());


        Map<String, List<BaseCareerResponse>> groupedCareers = baseCareers.stream()
                .collect(Collectors.groupingBy(
                        career -> String.valueOf(career.getEndDate().getYear())
                ));


        Map<String, List<?>> result = new HashMap<>();
        for (Map.Entry<String, List<BaseCareerResponse>> entry : groupedCareers.entrySet()) {
            List<?> filteredList = entry.getValue().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            result.put(entry.getKey(), filteredList);
        }
        return result;
    }

    @Override
    public List<BaseCareerResponse> findAllCareer(Long memberId) {
        List<BaseCareerResponse> baseCareers = projectJpaRepository.findByMemberId(memberId).stream()
                .map(project-> new ProjectResponse(project,detailRepository.findByCareerIdAndCareerType(CareerType.PROJECT,project.getId())))
                .collect(Collectors.toList());
        baseCareers.addAll(competitionJpaRepository.findByMemberId(memberId).stream()
                .map(comp-> new CompetitionResponse(comp, detailRepository.findByCareerIdAndCareerType(CareerType.COM,comp.getId())))
                .collect(Collectors.toList()));
        baseCareers.addAll(activityRepository.findByMemberId(memberId).stream()
                .map(activity->new ActivityResponse(activity, detailRepository.findByCareerIdAndCareerType(CareerType.ACTIVITY,activity.getId())))
                .collect(Collectors.toList()));
        baseCareers.addAll(circleRepository.findByMemberId(memberId).stream()
                .map(circle-> new CircleResponse(circle, detailRepository.findByCareerIdAndCareerType(CareerType.CIRCLE,circle.getId())))
                .collect(Collectors.toList()));
        baseCareers.addAll(eduCareerJpaRepository.findByMemberId(memberId).stream()
                .map(eduCareer -> new EduCareerResponse(eduCareer, detailRepository.findByCareerIdAndCareerType(CareerType.EDU,eduCareer.getId())))
                .collect(Collectors.toList()));
        baseCareers.addAll(employmentJpaRepository.findByMemberId(memberId).stream()
                .map(employment -> new EmploymentResponse(employment, detailRepository.findByCareerIdAndCareerType(CareerType.EMP,employment.getId())))
                .collect(Collectors.toList()));

        baseCareers.addAll(etcRepository.findByMemberId(memberId).stream()
                .map(etc -> new EtcResponse(etc, detailRepository.findByCareerIdAndCareerType(CareerType.ETC,etc.getId())))
                .collect(Collectors.toList()));

        baseCareers = baseCareers.stream()
                .sorted(Comparator.comparing(BaseCareerResponse::getEndDate).reversed())
                .collect(Collectors.toList());

        return baseCareers;
    }
    @Override
    public BaseCareerResponse findCareer(Member requestMember, Long careerId, String type) {
        switch (type.toLowerCase()) {
            case "activity" -> {
                Activity activity = activityRepository.findById(careerId).get();
                if(!activity.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                return getResponse(activity, ActivityResponse::new);
            }
            case "circle"  -> {
                Circle circle = circleRepository.findById(careerId).get();
                if(!circle.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                return getResponse(circle, CircleResponse::new);
            }
            case "project"  -> {
                Project project = projectJpaRepository.findById(careerId).get();
                if(!project.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                return getResponse(project, ProjectResponse::new);
            }
            case "edu"  -> {
                EduCareer eduCareer = eduCareerJpaRepository.findById(careerId).get();
                if(!eduCareer.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                return getResponse(eduCareer, EduCareerResponse::new);
            }
            case "competition"  -> {
                Competition competition = competitionJpaRepository.findById(careerId).get();
                if(!competition.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                return getResponse(competition, CompetitionResponse::new);
            }
            case "employment"  -> {
                Employment employment = employmentJpaRepository.findById(careerId).get();
                if(!employment.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                return getResponse(employment, EmploymentResponse::new);
            }
            case "etc"  -> {
                CareerEtc etc = etcRepository.findById(careerId).get();
                if(!etc.getMemberId().equals(requestMember.getId())){
                    throw new OwnerMismatchException();
                }
                return getResponse(etc, EtcResponse::new);
            }
            default -> throw new IllegalArgumentException("지원하지 않는 활동 유형입니다 : " + type);
        }
    }

    @Override
    public FindTagResponse.SearchTagResponse findAllTag(Member requestMember, String keyword) {
        List<Tag> tags = tagRepository.findByKeywordAndMemberId(keyword, requestMember.getId());
        int detailCount = 0;
        Set<BaseCareerDetail> details = new HashSet<>();

        //1. 중복 고려 안하고 count
//        for (int i = 0; i < tags.size(); i++) {
//            Tag target = tags.get(i);
//            detailCount += detailRepository.findByTag(target.getId()).size();
//        }

        //2. 중복 제거하고 count
        for (int i = 0; i < tags.size(); i++) {
            Tag target = tags.get(i);
            detailRepository.findByTag(target.getId())
                    .stream()
                    .forEach(details::add);
        }
        detailCount = details.size();

        return BaseCareerConverter.toSearchTagResponse(tags,detailCount);
    }
    @Override
    public List<FindDetailResponse> findAllDetail(Member requestMember, String keyword, String sort) {
        List<BaseCareerDetail> detailList = detailRepository.findByMemberIdAndKeyword(requestMember.getId(), keyword);
        return buildDetailResponse(detailList, sort);
    }

    @Override
    public List<FindDetailResponse> findAllDetailByTag(Member requestMember, Long tagId, String sort) {
        Tag tag = tagRepository.findById(tagId).get();
        if (!tag.getMemberId().equals(requestMember.getId())) {
            throw new OwnerMismatchException();
        }
        List<BaseCareerDetail> detailList = detailRepository.findByTag(tag.getId());
        return buildDetailResponse(detailList, sort);
    }

    @Override
    public List<FindCareerResponse> findCareerWithKeyword(Member requestMember, String keyword, String sort) {
        List<BaseCareer> careers = new ArrayList<>();
        careers.addAll(activityRepository.findByMemberIdAndNameContaining(requestMember.getId(),keyword));
        careers.addAll(eduCareerJpaRepository.findByMemberIdAndNameContaining(requestMember.getId(),keyword));
        careers.addAll(employmentJpaRepository.findByMemberIdAndNameContaining(requestMember.getId(),keyword));
        careers.addAll(circleRepository.findByMemberIdAndNameContaining(requestMember.getId(),keyword));
        careers.addAll(projectJpaRepository.findByMemberIdAndNameContaining(requestMember.getId(),keyword));
        careers.addAll(competitionJpaRepository.findByMemberIdAndNameContaining(requestMember.getId(),keyword));
        careers.addAll(etcRepository.findByMemberIdAndNameContaining(requestMember.getId(),keyword));

        if ("new".equalsIgnoreCase(sort)) {
            careers.sort(Comparator.comparing(BaseCareer::getEnddate).reversed());
        } else {
            careers.sort(Comparator.comparing(BaseCareer::getEnddate));
        }

        return careers.stream()
                .map(career -> new FindCareerResponse(career.getId(), career.getName(), career.getAlias(),
                        career.getStartdate(), career.getEnddate(), CareerType.fromClass(career)))
                .collect(Collectors.toList());

    }




    //타임라인 쪽에서 데이터 구별 하기 위함
    private CareerType resolveCareerType(BaseCareer career) {
        if (career instanceof Activity) {
            return CareerType.ACTIVITY;
        } else if (career instanceof Project) {
            return CareerType.PROJECT;
        } else if (career instanceof Employment) {
            return CareerType.EMP;
        } else if (career instanceof EduCareer) {
            return CareerType.EDU;
        } else if (career instanceof Circle) {
            return CareerType.CIRCLE;
        } else if (career instanceof Competition) {
            return CareerType.COM;
        } else {
            return CareerType.ETC;
        }
    }
    //활동 기록 상세조회에서 사용
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
    private List<FindDetailResponse> buildDetailResponse(List<BaseCareerDetail> detailList, String sort) {
        Map<CareerType, Map<Long, List<BaseCareerDetail>>> groupedDetails = new HashMap<>();
        for (BaseCareerDetail detail : detailList) {
            CareerType careerType = getCareerType(detail);
            Long careerId = detail.getCareerId();
            groupedDetails
                    .computeIfAbsent(careerType, k -> new HashMap<>())
                    .computeIfAbsent(careerId, k -> new ArrayList<>())
                    .add(detail);
        }

        List<FindDetailResponse> result = new ArrayList<>();

        for(Map.Entry<CareerType, Map<Long, List<BaseCareerDetail>>> entry : groupedDetails.entrySet()) {
            CareerType type = entry.getKey();
            Map<Long, List<BaseCareerDetail>> careerMap = entry.getValue();

            for(Map.Entry<Long,List<BaseCareerDetail>> careerEntry : careerMap.entrySet()){
                Long careerId = careerEntry.getKey();
                List<BaseCareerDetail> details = careerEntry.getValue();
                List<BaseCareerDetailResponse> detailResponses = new ArrayList<>();
                CareerSearchServiceImpl.FindDetailInfo detailInfo = extractDetailInfo(details);

                for (BaseCareerDetail detail: details) {
                    detailResponses.add(new BaseCareerDetailResponse(detail));
                }

                result.add(new FindDetailResponse(careerId, detailInfo.title,detailInfo.alias,
                        detailInfo.startDate,detailInfo.endDate,
                        detailResponses,type));
            }
        }
        if (sort.equals("new")) {
            result.stream().sorted(Comparator.comparing(FindDetailResponse::getEndDate).reversed());
        } else {
            result.stream().sorted(Comparator.comparing(FindDetailResponse::getEndDate).reversed());
        }
        return result;

    }

    private CareerType getCareerType(BaseCareerDetail detail) {
        switch (detail.getCareerType()) {
            case ACTIVITY: return CareerType.ACTIVITY;
            case PROJECT: return CareerType.PROJECT;
            case EMP: return CareerType.EMP;
            case EDU: return CareerType.EDU;
            case COM: return CareerType.COM;
            case CIRCLE: return CareerType.CIRCLE;
            case ETC:return CareerType.ETC;
            default: return null;
        }
    }
//    private Long getCareerId(BaseCareerDetail detail) {
//        switch (detail.getCareerType()) {
//            case ACTIVITY: return detail.getActivity().getId();
//            case PROJECT: return detail.getProject().getId();
//            case EMP: return detail.getEmployment().getId();
//            case EDU: return detail.getEduCareer().getId();
//            case COM: return detail.getCompetition().getId();
//            case CIRCLE: return detail.getCircle().getId();
//            case ETC: return detail.getEtc().getId();
//            default: return null;
//        }
//    }
    private CareerSearchServiceImpl.FindDetailInfo extractDetailInfo(List<BaseCareerDetail> details) {
        if (details.isEmpty()) {
            return new CareerSearchServiceImpl.FindDetailInfo(null, null, null, null);
        }

        BaseCareerDetail firstDetail = details.get(0);
        String title = null;
        String alias = null;
        LocalDate startDate = null;
        LocalDate endDate = null;

        switch (firstDetail.getCareerType()) {
            case ACTIVITY -> {
                Activity activity = activityRepository.findById(firstDetail.getCareerId()).get();
                title = activity.getName();
                alias = activity.getAlias();
            }
            case PROJECT -> {
                Project project = projectJpaRepository.findById(firstDetail.getCareerId()).get();
                title = project.getName();
                alias = project.getAlias();
            }
            case EMP -> {
                Employment emp = employmentJpaRepository.findById(firstDetail.getCareerId()).get();
                title = emp.getName();
                alias = emp.getAlias();
            }
            case EDU -> {
                EduCareer edu = eduCareerJpaRepository.findById(firstDetail.getCareerId()).get();
                title = edu.getName();
                alias = edu.getAlias();
            }
            case COM -> {
                Competition competition = competitionJpaRepository.findById(firstDetail.getCareerId()).get();
                title = competition.getName();
                alias = competition.getAlias();
            }
            case CIRCLE -> {
                Circle circle = circleRepository.findById(firstDetail.getCareerId()).get();
                title = circle.getName();
                alias = circle.getAlias();
            }
            case ETC -> {
                CareerEtc etc = etcRepository.findById(firstDetail.getCareerId()).get();
                title = etc.getName();
                alias = etc.getAlias();
            }
        }

        startDate = firstDetail.getStartDate();
        endDate = firstDetail.getEndDate();

        return new CareerSearchServiceImpl.FindDetailInfo(title, alias, startDate, endDate);
    }
    private static class FindDetailInfo {
        String title;
        String alias;
        LocalDate startDate;
        LocalDate endDate;

        FindDetailInfo(String title, String alias, LocalDate startDate, LocalDate endDate) {
            this.title = title;
            this.alias = alias;
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

}
