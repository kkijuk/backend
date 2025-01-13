package umc.kkijuk.server.introduce.service;

import umc.kkijuk.server.introduce.controller.response.*;
import umc.kkijuk.server.introduce.domain.Introduce;
import umc.kkijuk.server.introduce.dto.IntroduceReqDto;
import umc.kkijuk.server.member.domain.Member;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IntroduceService {

    IntroduceResponse saveIntro(Member requestMember, Long recruitId, IntroduceReqDto introduceReqDto);
    IntroduceResponse getIntro(Member requestMember, Long introId);
    List<IntroduceListResponse> getIntroList(Member requestMember);
    IntroduceResponse updateIntro(Member requestMember, Long introId, IntroduceReqDto introduceReqDto) throws Exception;
    Long deleteIntro(Member requestMember, Long introId);
    Map<String, Object> searchIntroduceAndMasterByKeyword(String keyword, Member requestMember);
    int findStateByRecruitId(Long recruitId);
}
