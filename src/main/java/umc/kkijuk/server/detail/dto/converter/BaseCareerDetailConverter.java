package umc.kkijuk.server.detail.dto.converter;

import umc.kkijuk.server.career.domain.*;
import umc.kkijuk.server.detail.domain.BaseCareerDetail;
import umc.kkijuk.server.detail.dto.CareerDetailReqDto;
import umc.kkijuk.server.member.domain.Member;

import java.util.ArrayList;

public class BaseCareerDetailConverter {
    public static BaseCareerDetail toBaseCareerDetail(Member requestMember, CareerDetailReqDto request, Long careerId) {
        return BaseCareerDetail.builder()
                .careerType(request.getCareerType())
                .memberId(requestMember.getId())
                .careerId(careerId)
                .title(request.getTitle())
                .content(request.getContent())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .careerTagList(new ArrayList<>())
                .build();
//        if(baseCareer instanceof Activity){
//            return BaseCareerDetail.builder()
//                    .activity((Activity) baseCareer)
//                    .careerType(request.getCareerType())
//                    .memberId(requestMember.getId())
//                    .title(request.getTitle())
//                    .content(request.getContent())
//                    .startDate(request.getStartDate())
//                    .endDate(request.getEndDate())
//                    .careerTagList(new ArrayList<>())
//                    .build();
//
//        }else if ( baseCareer instanceof Project){
//            return BaseCareerDetail.builder()
//                    .project((Project) baseCareer)
//                    .careerType(request.getCareerType())
//                    .memberId(requestMember.getId())
//                    .title(request.getTitle())
//                    .content(request.getContent())
//                    .startDate(request.getStartDate())
//                    .endDate(request.getEndDate())
//                    .careerTagList(new ArrayList<>())
//                    .build();
//        }else if ( baseCareer instanceof Circle){
//            return BaseCareerDetail.builder()
//                    .circle((Circle) baseCareer)
//                    .careerType(request.getCareerType())
//                    .memberId(requestMember.getId())
//                    .title(request.getTitle())
//                    .content(request.getContent())
//                    .startDate(request.getStartDate())
//                    .endDate(request.getEndDate())
//                    .careerTagList(new ArrayList<>())
//                    .build();
//        }else if ( baseCareer instanceof Employment){
//            return BaseCareerDetail.builder()
//                    .employment((Employment) baseCareer)
//                    .careerType(request.getCareerType())
//                    .memberId(requestMember.getId())
//                    .title(request.getTitle())
//                    .content(request.getContent())
//                    .startDate(request.getStartDate())
//                    .endDate(request.getEndDate())
//                    .careerTagList(new ArrayList<>())
//                    .build();
//        }else if ( baseCareer instanceof EduCareer){
//            return BaseCareerDetail.builder()
//                    .eduCareer((EduCareer) baseCareer)
//                    .careerType(request.getCareerType())
//                    .memberId(requestMember.getId())
//                    .title(request.getTitle())
//                    .content(request.getContent())
//                    .startDate(request.getStartDate())
//                    .endDate(request.getEndDate())
//                    .careerTagList(new ArrayList<>())
//                    .build();
//        }else{
//            return BaseCareerDetail.builder()
//                    .competition((Competition) baseCareer)
//                    .careerType(request.getCareerType())
//                    .memberId(requestMember.getId())
//                    .title(request.getTitle())
//                    .content(request.getContent())
//                    .startDate(request.getStartDate())
//                    .endDate(request.getEndDate())
//                    .careerTagList(new ArrayList<>())
//                    .build();
//
//        }

    }


}
