package umc.kkijuk.server.member.service;

import umc.kkijuk.server.auth.dto.AuthResponse;
import umc.kkijuk.server.auth.dto.NaverUserResponse;
import umc.kkijuk.server.member.controller.response.MemberEmailResponse;
import umc.kkijuk.server.member.controller.response.MemberInfoResponse;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.dto.*;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member getById(Long memberId);
    List<String> getMemberField(Long memberId);
    Member updateMemberField(Long memberId, MemberFieldDto memberFieldDto);
    Member updateMemberInfo(Long memberId, MemberInfoChangeDto memberInfoChangeDto);
    MemberEmailResponse getMemberEmail(Member member);
    List<String> addRecruitTag(Member member, String tag);
    List<String> deleteRecruitTag(Member Member, String tag);
    Member createUserWithKakaoId(String kakaoId, Map<String, Object> kakaoUserInfo);
    MemberInfoResponse getMemberInfo(Long memberId);
    Long extractMemberId(String bearerToken);
    public Member findBySocialId(String SocialId);
    Member createUserWithNaverId(String naverId, NaverUserResponse.NaverUserDetail naverUserInfo);
    Member completeProfile(Long memberId, ProfileInputDto profileInputDto);
    void memberInactivation(Long memberId, String token);
}
