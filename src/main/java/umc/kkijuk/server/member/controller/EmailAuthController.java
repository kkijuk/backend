package umc.kkijuk.server.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.member.emailauth.MailAddressDto;
import umc.kkijuk.server.member.emailauth.MailCertificationDto;
import umc.kkijuk.server.member.emailauth.MailCertificationResponse;
import umc.kkijuk.server.member.emailauth.MailServiceImpl;
import umc.kkijuk.server.member.service.MemberService;

@Tag(name = "auth", description = "이메일 인증 API")
@RestController
@RequiredArgsConstructor
public class EmailAuthController {

    private final MailServiceImpl mailService;
    private final MemberService memberService;

    @Operation(
            summary = "회원가입시 이메일 인증번호 요청",
            description = "이메일 인증정보를 요청합니다.")
    @PostMapping("/auth")
    public ResponseEntity<MailCertificationResponse> joinSendMail(@RequestBody @Valid MailAddressDto mailAddressDto) {
        MailCertificationResponse mailCertificationResponse = mailService.sendMailJoin(mailAddressDto.getEmail());
        return ResponseEntity.ok(mailCertificationResponse);
    }

    @Operation(
            summary = "비밀번호 재설정시 이메일 인증번호 요청",
            description = "이메일 인증정보를 요청합니다.")
    @PostMapping("/password/send")
    public ResponseEntity<MailCertificationResponse> resetPasswordSendMail(@RequestBody @Valid MailAddressDto mailAddressDto) {
        MailCertificationResponse mailCertificationResponse = mailService.sendMailPasswordReset(mailAddressDto.getEmail());
        return ResponseEntity.ok(mailCertificationResponse);
    }

    @Operation(
            summary = "이메일 인증번호 인증",
            description = "이메일 인증번호를 인증합니다.")
    @PostMapping({"/auth/confirm", "/password/confirm"})
    public ResponseEntity<Boolean> confirmMailNumber(@RequestBody @Valid MailCertificationDto mailCertificationDto){
        return ResponseEntity.ok(mailService.verifyMail(mailCertificationDto));
    }

//    @Operation(
//            summary = "회원 비밀번호 재설정",
//            description = "회원의 비밀번호를 새로운 값으로 재설정합니다.")
//    @PostMapping("/password/reset")
//    public ResponseEntity<Boolean> resetMemberPassword(@RequestBody @Valid MemberPasswordResetDto memberPasswordResetDto){
//
//        Member member = memberService.resetMemberPassword(memberPasswordResetDto);
//        return ResponseEntity.ok(Boolean.TRUE);
//    }

}
