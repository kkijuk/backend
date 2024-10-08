package umc.kkijuk.server.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.common.LoginUser;
import umc.kkijuk.server.login.argumentresolver.Login;
import umc.kkijuk.server.login.controller.dto.LoginInfo;
import umc.kkijuk.server.login.service.LoginService;
import umc.kkijuk.server.member.controller.response.*;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.dto.*;
import umc.kkijuk.server.member.emailauth.MailServiceImpl;
import umc.kkijuk.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Tag(name = "member", description = "회원 관리 API")
@Builder
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MailServiceImpl mailService;
    private final LoginService loginService;

    @Operation(
            summary = "회원가입 요청",
            description = "회원가입 요청을 받아 성공/실패 여부를 반환합니다.")
    @PostMapping
    public ResponseEntity<CreateMemberResponse> saveMember(
            @RequestBody @Valid MemberJoinDto memberJoinDto,
            HttpServletRequest request,
            HttpServletResponse response) {
        Member joinMember = memberService.join(memberJoinDto);

        loginService.makeLoginSession(LoginInfo.from(joinMember), request, response);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateMemberResponse(joinMember.getId(), "Member created successfully"));
    }


    @Operation(
            summary = "이메일 중복 확인",
            description = "회원가입시 이메일 중복을 확인합니다.")
    @PostMapping("/confirmEmail")
    public ResponseEntity<Boolean> confirmEmail(@RequestBody MemberEmailDto memberEmailDto){
        Boolean result = mailService.confirmDupEmail(memberEmailDto);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "내 정보 조회",
            description = "마이페이지에서 내 정보들을 가져옵니다.")
    @GetMapping("/myPage/info")
    public ResponseEntity<MemberInfoResponse> getInfo(@Login LoginInfo loginInfo) {
//        Long loginUser = LoginUser.get().getId();
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(loginInfo.getMemberId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberInfoResponse);
    }

    @Operation(
            summary = "내 정보 수정",
            description = "내 정보 수정 요청을 받아 성공/실패를 반환합니다.")
    @PutMapping("/myPage/info")
    public ResponseEntity<Boolean> changeMemberInfo(@Login LoginInfo loginInfo,
                                                    @RequestBody @Valid  MemberInfoChangeDto memberInfoChangeDto) {
//        Long loginUser = LoginUser.get().getId();
        memberService.updateMemberInfo(loginInfo.getMemberId(), memberInfoChangeDto);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @Operation(
            summary = "관심분야 조회",
            description = "마이페이지에서 관심분야를 조회합니다.")
    @GetMapping("/myPage/field")
    public ResponseEntity<MemberFieldResponse> getField(@Login LoginInfo loginInfo) {
//        Long loginUser = LoginUser.get().getId();
        List<String> memberField = memberService.getMemberField(loginInfo.getMemberId());
        return ResponseEntity.ok().body(new MemberFieldResponse(memberField));
    }

    @Operation(
            summary = "관심분야 등록/수정",
            description = "초기/마이페이지에서 관심분야를 등록/수정합니다.")
    @PostMapping({"/field", "/myPage/field"})
    public ResponseEntity<Boolean> postField(@Login LoginInfo loginInfo,
                                             @RequestBody MemberFieldDto memberFieldDto) {
//        Long loginUser = LoginUser.get().getId();
        memberService.updateMemberField(loginInfo.getMemberId(), memberFieldDto);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "비밀번호를 변경합니다.")
    @PostMapping("myPage/password")
    public ResponseEntity<Boolean> changeMemberPassword(@Login LoginInfo loginInfo,
                                                        @RequestBody @Valid MemberPasswordChangeDto memberPasswordChangeDto){
//        Long loginUser = LoginUser.get().getId();
        memberService.changeMemberPassword(loginInfo.getMemberId(), memberPasswordChangeDto);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @Operation(
            summary = "내정보 조회 인증 화면 이메일 가져오기",
            description = "내 정보를 조회 인증 화면에서 이메일을 가져옵니다.")
    @GetMapping("/myPage")
    public ResponseEntity<MemberEmailResponse> getEmail(@Login LoginInfo loginInfo) {
//        Long loginUser = LoginUser.get().getId();
        MemberEmailResponse memberEmailResponse = memberService.getMemberEmail(loginInfo.getMemberId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberEmailResponse);
    }


    @Operation(
            summary = "내정보 조회용 비밀번호 인증",
            description = "내 정보를 조회하기 위해 비밀번호를 인증합니다.")
    @PostMapping("/myPage")
    public ResponseEntity<Boolean> myPagePasswordAuth(@Login LoginInfo loginInfo,
                                                      @RequestBody @Valid MyPagePasswordAuthDto myPagePasswordAuthDto){
//        Long loginUser = LoginUser.get().getId();
        memberService.myPagePasswordAuth(loginInfo.getMemberId(), myPagePasswordAuthDto);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "회원의 상태를 비활성화로 바꿉니다.")
    @PatchMapping("/inactive")
    public ResponseEntity<MemberStateResponse> memberInactivate(@Login LoginInfo loginInfo){
//        Long loginUser = LoginUser.get().getId();
        MemberStateResponse memberStateResponse = memberService.changeMemberState(loginInfo.getMemberId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberStateResponse);
    }

}






