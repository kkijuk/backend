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

    @Operation(
            summary = "이메일 인증번호 요청",
            description = "이메일 수정 시 이메일 인증정보를 요청합니다.")
    @PostMapping("/auth")
    public ResponseEntity<MailCertificationResponse> joinSendMail(@Valid @RequestBody MailAddressDto mailAddressDto) {
        MailCertificationResponse mailCertificationResponse = mailService.sendMailJoin(mailAddressDto.getEmail());
        return ResponseEntity.ok(mailCertificationResponse);
    }

    @Operation(
            summary = "이메일 인증번호 확인",
            description = "이메일 인증번호의 일치 여부를 확인합니다.")
    @PostMapping("/auth/confirm")
    public ResponseEntity<Boolean> confirmMailNumber(@Valid @RequestBody MailCertificationDto mailCertificationDto){
        return ResponseEntity.ok(mailService.verifyMail(mailCertificationDto));
    }


}
