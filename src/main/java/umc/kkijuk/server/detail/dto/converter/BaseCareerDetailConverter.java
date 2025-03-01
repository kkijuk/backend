package umc.kkijuk.server.detail.dto.converter;

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


}
