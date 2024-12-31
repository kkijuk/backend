package umc.kkijuk.server.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.auth.dto.AuthResponse;
import umc.kkijuk.server.auth.dto.RefreshTokenRequest;
import umc.kkijuk.server.auth.jwt.JwtUtil;
import umc.kkijuk.server.auth.service.AuthService;
import umc.kkijuk.server.common.LoginUser;
import umc.kkijuk.server.member.controller.response.*;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.dto.*;
import umc.kkijuk.server.member.emailauth.MailServiceImpl;
import umc.kkijuk.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(name = "member", description = "회원 관리 API")
@Builder
@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;
    private final MailServiceImpl mailService;
    private final JwtUtil jwtUtil;
    private final LoginUser loginUser;

//    @Operation(
//            summary = "회원가입 요청",
//            description = "회원가입 요청을 받아 성공/실패 여부를 반환합니다.")
//    @PostMapping
//    public ResponseEntity<CreateMemberResponse> saveMember(
//            @RequestBody @Valid MemberJoinDto memberJoinDto,
//            HttpServletRequest request,
//            HttpServletResponse response) {
//        Member joinMember = memberService.join(memberJoinDto);
//
//        loginService.makeLoginSession(LoginInfo.from(joinMember), request, response);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(new CreateMemberResponse(joinMember.getId(), "Member created successfully"));
//    }


//    @Operation(
//            summary = "이메일 중복 확인",
//            description = "회원가입시 이메일 중복을 확인합니다.")
//    @PostMapping("/confirmEmail")
//    public ResponseEntity<Boolean> confirmEmail(@RequestBody MemberEmailDto memberEmailDto){
//        Boolean result = mailService.confirmDupEmail(memberEmailDto);
//        return ResponseEntity.ok(result);
//    }


//    @Operation(
//            summary = "관심분야 조회",
//            description = "마이페이지에서 관심분야를 조회합니다.")
//    @GetMapping("/myPage/field")
//    public ResponseEntity<MemberFieldResponse> getField() {
//        Long loginUser = LoginUser.get().getId();
//        List<String> memberField = memberService.getMemberField(loginUser);
//        return ResponseEntity.ok().body(new MemberFieldResponse(memberField));
//    }
//
//    @Operation(
//            summary = "관심분야 등록/수정",
//            description = "초기/마이페이지에서 관심분야를 등록/수정합니다.")
//    @PostMapping({"/field", "/myPage/field"})
//    public ResponseEntity<Boolean> postField(@RequestBody MemberFieldDto memberFieldDto) {
//        Long loginUser = LoginUser.get().getId();
//        memberService.updateMemberField(loginUser, memberFieldDto);
//        return ResponseEntity.ok(Boolean.TRUE);
//    }

//    @Operation(
//            summary = "비밀번호 변경",
//            description = "비밀번호를 변경합니다.")
//    @PostMapping("myPage/password")
//    public ResponseEntity<Boolean> changeMemberPassword(@RequestBody @Valid MemberPasswordChangeDto memberPasswordChangeDto){
//        Long loginUser = LoginUser.get().getId();
//        memberService.changeMemberPassword(loginUser, memberPasswordChangeDto);
//        return ResponseEntity.ok(Boolean.TRUE);
//    }

//    @Operation(
//            summary = "내정보 조회 인증 화면 이메일 가져오기",
//            description = "내 정보를 조회 인증 화면에서 이메일을 가져옵니다.")
//    @GetMapping("/myPage")
//    public ResponseEntity<MemberEmailResponse> getEmail() {
//        Long loginUser = LoginUser.get().getId();
//        MemberEmailResponse memberEmailResponse = memberService.getMemberEmail(loginUser);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(memberEmailResponse);
//    }


//    @Operation(
//            summary = "내정보 조회용 비밀번호 인증",
//            description = "내 정보를 조회하기 위해 비밀번호를 인증합니다.")
//    @PostMapping("/myPage")
//    public ResponseEntity<Boolean> myPagePasswordAuth(@RequestBody @Valid MyPagePasswordAuthDto myPagePasswordAuthDto){
//        Long loginUser = LoginUser.get().getId();
//        memberService.myPagePasswordAuth(loginUser, myPagePasswordAuthDto);
//        return ResponseEntity.ok(Boolean.TRUE);
//    }

//    @Operation(
//            summary = "회원 탈퇴",
//            description = "회원의 상태를 비활성화로 바꿉니다.")
//    @PatchMapping("/inactive")
//    public ResponseEntity<MemberStateResponse> memberInactivate(){
//        Long loginUser = LoginUser.get().getId();
//        MemberStateResponse memberStateResponse = memberService.changeMemberState(loginUser);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(memberStateResponse);
//    }

    /**
     * 소셜로그인 이후 필요한 api
     */


    @Operation(summary = "액세스 토큰 재발급",
            description = "Refresh Token을 받아서 새로운 Access,Refresh Token을 발급(Refresh Token Rotation)")
    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();
        String socialId = jwtUtil.extractSocialId(refreshToken);

        AuthResponse response = memberService.refreshAuthToken(refreshToken, socialId);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "내 정보 조회",
            description = "마이페이지에서 내 정보들을 가져옵니다.")
    @GetMapping("/myPage/info")
    public ResponseEntity<MemberInfoResponse> getInfo(@RequestHeader("Authorization") String token) {
        Long memberId = loginUser.extractMemberId(token);
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberInfoResponse);
    }

    @Operation(
            summary = "내 정보 수정",
            description = "내 정보 수정 요청을 받아 성공/실패를 반환합니다.")
    @PutMapping("/myPage/info")
    public ResponseEntity<Boolean> changeMemberInfo(@RequestHeader("Authorization") String token,
                                                    @RequestBody @Valid  MemberInfoChangeDto memberInfoChangeDto) {
        Long memberId = loginUser.extractMemberId(token);
        memberService.updateMemberInfo(memberId, memberInfoChangeDto);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @Operation(
            summary = "관심분야 조회",
            description = "마이페이지에서 관심분야를 조회합니다.")
    @GetMapping("/myPage/field")
    public ResponseEntity<MemberFieldResponse> getField(@RequestHeader("Authorization") String token) {
        Long memberId = loginUser.extractMemberId(token);
        List<String> memberField = memberService.getMemberField(memberId);
        return ResponseEntity.ok().body(new MemberFieldResponse(memberField));
    }

    @Operation(
            summary = "관심분야 등록/수정",
            description = "초기/마이페이지에서 관심분야를 등록/수정합니다.")
    @PostMapping({"/field", "/myPage/field"})
    public ResponseEntity<Boolean> postField(@RequestHeader("Authorization") String token,
                                             @RequestBody MemberFieldDto memberFieldDto) {
        Long memberId = loginUser.extractMemberId(token);
        memberService.updateMemberField(memberId, memberFieldDto);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String kakaoId = jwtUtil.extractSocialId(token.substring(7));
        memberService.invalidateRefreshToken(kakaoId);
        return ResponseEntity.ok("로그아웃 완료");
    }

    @Operation(
            summary = "추가 정보 입력",
            description = "소셜 로그인 후 사용자에게 추가 정보(이용약관 동의, 개인정보 수집 및 이용 동의," +
                    "마케팅 정보 수신 동의, 사용자 직업 )를 입력받아 저장합니다."
    )
    @PostMapping("/profile")
    public ResponseEntity<Map<String, Object>> addProfile(@RequestHeader("Authorization") String token,
                                                   @RequestBody @Valid ProfileInputDto profileInputDto){
        Long memberId = loginUser.extractMemberId(token);
        Member member = memberService.completeProfile(memberId, profileInputDto);

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("Token", authService.generateTokens(member));

        return ResponseEntity.ok(tokens);
    }

//    @Operation(summary = "계정 탈퇴", description = "계정 탈퇴 처리")
//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteAccount(@RequestHeader("Authorization") String token) {
//        Long kakaoId = jwtUtil.extractKakaoId(token.substring(7));
//        memberService.deleteAccount(kakaoId);
//        return ResponseEntity.ok("계정이 탈퇴되었습니다.");
//    }
}






