package umc.kkijuk.server.member.service;

import umc.kkijuk.server.auth.dto.AuthResponse;
import umc.kkijuk.server.auth.dto.NaverUserResponse;
import umc.kkijuk.server.member.controller.response.MemberEmailResponse;
import umc.kkijuk.server.member.controller.response.MemberInfoResponse;
import umc.kkijuk.server.member.controller.response.MemberStateResponse;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.dto.*;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member getById(Long memberId);
//    Member join(MemberJoinDto memberJoinDto);
    List<String> getMemberField(Long memberId);
    Member updateMemberField(Long memberId, MemberFieldDto memberFieldDto);
    Member updateMemberInfo(Long memberId, MemberInfoChangeDto memberInfoChangeDto);
//    Member changeMemberPassword(Long memberId, MemberPasswordChangeDto memberPasswordChangeDto);
//    Member myPagePasswordAuth(Long memberId, MyPagePasswordAuthDto myPagePasswordAuthDto);
    MemberEmailResponse getMemberEmail(Long memberId);
    MemberStateResponse changeMemberState(Long memberId);
//    Member resetMemberPassword(MemberPasswordResetDto memberPasswordResetDto);
    Boolean confirmDupEmail(MemberEmailDto memberEmailDto);
    List<String> addRecruitTag(Member member, String tag);
    List<String> deleteRecruitTag(Member Member, String tag);
    /**
     * 소셜로그인 이후 추가된 기능
     */
//    Member createUserWithKakaoId(Long socialId, Map<String, Object> kakaoUserInfo);
//    MemberInfoResponse getMemberInfo(Long memberId);
//    void invalidateRefreshToken(Long socialId);
//    void updateRefreshToken(Long socialId, String refreshToken);
//    Long extractMemberId(String bearerToken);
//    Member findBySocialId(Long socialId);
//    AuthResponse refreshAuthToken(String refreshToken, Long socialId);

    Member createUserWithKakaoId(String kakaoId, Map<String, Object> kakaoUserInfo);
    MemberInfoResponse getMemberInfo(Long memberId);
    void invalidateRefreshToken(String socialId);
    void updateRefreshToken(String socialId, String refreshToken);
    Long extractMemberId(String bearerToken);
    public Member findBySocialId(String SocialId);
    AuthResponse refreshAuthToken(String refreshToken, String socialId);
    Member createUserWithNaverId(String naverId, NaverUserResponse.NaverUserDetail naverUserInfo);
}
