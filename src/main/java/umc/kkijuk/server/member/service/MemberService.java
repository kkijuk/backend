package umc.kkijuk.server.member.service;

import umc.kkijuk.server.member.controller.response.EmailAuthResponse;
import umc.kkijuk.server.member.controller.response.MemberEmailResponse;
import umc.kkijuk.server.member.controller.response.MemberInfoResponse;
import umc.kkijuk.server.member.controller.response.MemberStateResponse;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MemberService {
    Member getById(Long memberId);
//    Member join(MemberJoinDto memberJoinDto);
    MemberInfoResponse getMemberInfo(Long memberId);
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
    Member createUserWithKakaoId(Long kakaoId, Map<String, Object> kakaoUserInfo);

}
