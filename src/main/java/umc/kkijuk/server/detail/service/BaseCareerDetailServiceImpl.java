package umc.kkijuk.server.detail.service;


import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.career.domain.*;
import umc.kkijuk.server.career.repository.*;
import umc.kkijuk.server.common.domian.exception.*;
import umc.kkijuk.server.detail.controller.response.BaseCareerDetailResponse;
import umc.kkijuk.server.detail.domain.*;
import umc.kkijuk.server.detail.domain.mapping.CareerDetailTag;
import umc.kkijuk.server.detail.dto.CareerDetailReqDto;
import umc.kkijuk.server.detail.dto.CareerDetailUpdateReqDto;
import umc.kkijuk.server.detail.dto.converter.*;
import umc.kkijuk.server.detail.repository.*;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.tag.domain.Tag;
import umc.kkijuk.server.tag.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Builder
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BaseCareerDetailServiceImpl implements BaseCareerDetailService{
    private final CareerDetailRepository careerDetailRepository;
    private final CareerDetailTagRepository careerDetailTagRepository;
    private final ActivityRepository activityRepository;
    private final EduCareerRepository eduCareerJpaRepository;
    private final ProjectRepository projectJpaRepository;
    private final CircleRepository circleRepository;
    private final EmploymentRepository employmentJpaRepository;
    private final CompetitionRepository competitionJpaRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public BaseCareerDetailResponse createDetail(Member requestMember, CareerDetailReqDto request, Long careerId) {
        BaseCareer career = findBaseCareerByType(request.getCareerType(), careerId);
        validateOwner(career, requestMember);

        List<CareerDetailTag> detailTagList = returnCareerTagList(request.getTagList());
        BaseCareerDetail newBaseCareerDetail = BaseCareerDetailConverter.toBaseCareerDetail(requestMember, request, careerId);

//        addDetailToCareer(career, newBaseCareerDetail);
        detailTagList.forEach(tag -> tag.setBaseCareerDetail(newBaseCareerDetail));
        return new BaseCareerDetailResponse(careerDetailRepository.save(newBaseCareerDetail));
    }

    @Override
    @Transactional
    public void deleteDetail(Member requestMember, Long careerId, Long detailId) {
        BaseCareerDetail baseCareerDetail = careerDetailRepository.findById(detailId).orElseThrow(
                () -> new ResourceNotFoundException("BaseCareerDetail", detailId));

        BaseCareer baseCareer = findBaseCareerByType(baseCareerDetail.getCareerType(), careerId);
        validateOwner(baseCareer,requestMember);

        careerDetailRepository.delete(baseCareerDetail);
    }

    @Override
    @Transactional
    public BaseCareerDetailResponse updateDetail(Member requestMember, CareerDetailUpdateReqDto request, Long careerId, Long detailId) {
        BaseCareerDetail baseCareerDetail = careerDetailRepository.findById(detailId).orElseThrow(
                () -> new ResourceNotFoundException("BaseCareerDetail", detailId));

        if(!baseCareerDetail.getMemberId().equals(requestMember.getId())){
            throw new OwnerMismatchException();
        }

        List<CareerDetailTag> existTags = new ArrayList<>(baseCareerDetail.getCareerTagList());
        baseCareerDetail.getCareerTagList().clear();
        existTags.forEach(careerDetailTag -> careerDetailTagRepository.delete(careerDetailTag));

        baseCareerDetail.updateBaseCareerDetail(
                request.getTitle(),
                request.getContent(),
                request.getStartDate(),
                request.getEndDate()
        );

        List<CareerDetailTag> careerDetailTags = returnCareerTagList(request.getTagList());
        careerDetailTags.forEach(careerDetailTag -> careerDetailTag.setBaseCareerDetail(baseCareerDetail));
        return new BaseCareerDetailResponse(careerDetailRepository.save(baseCareerDetail));
    }

    private void validateOwner(BaseCareer career, Member requestMember) {
        if (!career.getMemberId().equals(requestMember.getId())) {
            throw new OwnerMismatchException();
        }
    }

//    private void addDetailToCareer(BaseCareer career, BaseCareerDetail detail) {
//        if (career instanceof Competition) {
//            ((Competition) career).getDetailList().add(detail);
//        } else if (career instanceof Activity) {
//            ((Activity) career).getDetailList().add(detail);
//        } else if (career instanceof EduCareer) {
//            ((EduCareer) career).getDetailList().add(detail);
//        } else if (career instanceof Employment) {
//            ((Employment) career).getDetailList().add(detail);
//        } else if (career instanceof Circle) {
//            ((Circle) career).getDetailList().add(detail);
//        } else if (career instanceof Project) {
//            ((Project) career).getDetailList().add(detail);
//        } else {
//            throw new IllegalArgumentException("지원하지 않는 활동 유형입니다.");
//        }
//    }
    private BaseCareer findBaseCareerByType(CareerType careerType, Long careerId) {
        return switch (careerType) {
            case ACTIVITY -> activityRepository.findById(careerId)
                    .orElseThrow(() -> new ResourceNotFoundException("대외활동 : ", careerId));
            case CIRCLE -> circleRepository.findById(careerId)
                    .orElseThrow(() -> new ResourceNotFoundException("동아리 : ", careerId));
            case PROJECT -> projectJpaRepository.findById(careerId)
                    .orElseThrow(() -> new ResourceNotFoundException("프로젝트 : ", careerId));
            case EDU -> eduCareerJpaRepository.findById(careerId)
                    .orElseThrow(() -> new ResourceNotFoundException("교육 : ", careerId));
            case COM -> competitionJpaRepository.findById(careerId)
                    .orElseThrow(() -> new ResourceNotFoundException("대회 : ", careerId));
            case EMP -> employmentJpaRepository.findById(careerId)
                    .orElseThrow(() -> new ResourceNotFoundException("경력 : ", careerId));
            default -> throw new IllegalArgumentException("지원하지 않는 활동 유형입니다");
        };
    }

   private List<CareerDetailTag> returnCareerTagList(List<Long> tagIdList) {
        List<Tag> tagList = tagIdList.stream().map(tagId -> {
            return tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag", tagId));
        }).collect(Collectors.toList());
        return CareerDetailTagConverter.toCareerDetailTagList(tagList);
    }
}