package umc.kkijuk.server.detail.service;

import java.util.List;
import umc.kkijuk.server.career.controller.response.FindDetailResponse;
import umc.kkijuk.server.detail.controller.response.BaseCareerDetailResponse;
import umc.kkijuk.server.detail.controller.response.RecentCareerDetailResponse;
import umc.kkijuk.server.detail.dto.CareerDetailReqDto;
import umc.kkijuk.server.detail.dto.CareerDetailUpdateReqDto;
import umc.kkijuk.server.member.domain.Member;

public interface BaseCareerDetailService {
    BaseCareerDetailResponse createDetail(Member requestMember, CareerDetailReqDto request, Long careerId);

    void deleteDetail(Member requestMember, Long careerId, Long detailId);

    BaseCareerDetailResponse updateDetail(Member requestMember, CareerDetailUpdateReqDto request, Long careerId, Long detailId);

   List<RecentCareerDetailResponse>  boardDetail(Member requestMember);
}