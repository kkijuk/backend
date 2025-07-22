package umc.kkijuk.server.detail.dto.converter;

import java.util.stream.Collectors;
import umc.kkijuk.server.career.controller.response.CategoryResponse;
import umc.kkijuk.server.career.domain.BaseCareer;
import umc.kkijuk.server.detail.controller.response.RecentCareerDetailResponse;
import umc.kkijuk.server.detail.controller.response.TagResponse;
import umc.kkijuk.server.detail.domain.BaseCareerDetail;
import umc.kkijuk.server.detail.domain.CareerType;
import umc.kkijuk.server.detail.dto.CareerDetailReqDto;
import umc.kkijuk.server.member.domain.Member;

import java.util.ArrayList;

public class BaseCareerDetailConverter {

    public static BaseCareerDetail toBaseCareerDetail(Member requestMember, CareerDetailReqDto request, Long careerId, CareerType type) {

        return BaseCareerDetail.builder()
            .careerType(type)
            .memberId(requestMember.getId())
            .careerId(careerId)
            .title(request.getTitle())
            .content(request.getContent())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .careerTagList(new ArrayList<>())
            .build();

    }

    public static RecentCareerDetailResponse toResponse(BaseCareerDetail detail, BaseCareer baseCareer, CareerType type) {
        return new RecentCareerDetailResponse(
            detail.getId(),
            detail.getTitle(),
            detail.getContent(),
            detail.getStartDate(),
            detail.getEndDate(),
            detail.getCareerTagList().stream()
                .map(tag -> new TagResponse(tag.getTag()))
                .collect(Collectors.toList()),
            baseCareer.getId(),
            baseCareer.getName(),
            baseCareer.getAlias(),
            new CategoryResponse(type.getId(), type.getDescription(), type.name())
        );
    }


}
