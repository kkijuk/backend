package umc.kkijuk.server.career.service;

import umc.kkijuk.server.career.controller.response.*;
import umc.kkijuk.server.member.domain.Member;

import java.util.List;
import java.util.Map;

public interface CareerSearchService {
    List<TimelineResponse> findCareerForTimeline(Member requestMember);
    Map<String, List<?>> findAllCareerGroupedCategory(Long memberId);
    Map<String, List<?>> findAllCareerGroupedYear(Long memberId);
    List<BaseCareerResponse> findAllCareer(Long memberId);
    BaseCareerResponse findCareer(Member requestMember, Long careerId, String type);
    List<FindDetailResponse> findAllDetail(Member requestMember, String keyword, String sort);

    FindTagResponse.SearchTagResponse findAllTag(Member requestMember, String keyword);

    List<FindDetailResponse> findAllDetailByTag(Member requestMember, Long tagId, String sort);

    List<FindCareerResponse> findCareerWithKeyword(Member requestMember, String keyword, String sort);

}
