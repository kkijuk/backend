package umc.kkijuk.server.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import umc.kkijuk.server.common.domian.exception.CertificationNumberMismatchException;
import umc.kkijuk.server.common.domian.exception.MemberAlreadyExistsException;
import umc.kkijuk.server.member.domain.MarketingAgree;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.member.dto.MemberEmailDto;
import umc.kkijuk.server.member.emailauth.*;
import umc.kkijuk.server.member.mock.FakeMemberRepository;
import umc.kkijuk.server.member.mock.FakePasswordEncoder;
import umc.kkijuk.server.member.repository.MemberRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

public class MailServiceTest {

    private MailService mailService;
    private MailCertification mailCertification;
    private JavaMailSender javaMailSender;
    private MemberRepository memberRepository;

    private final String testMemberEmail = "test@naver.com";

    @BeforeEach
    void init() {
        mailCertification = mock(MailCertification.class);
        javaMailSender = mock(JavaMailSender.class);
        memberRepository = new FakeMemberRepository();

        this.mailService = new MailServiceImpl(mailCertification, javaMailSender, memberRepository);

        Member member = Member.builder()
                .email(testMemberEmail)
                .name("홍길동")
                .phoneNumber("01012345678")
                .birthDate(LocalDate.of(1999, 3, 31))
//                .password("testpassword")
                .marketingAgree(MarketingAgree.BOTH)
                .userState(State.ACTIVATE)
                .field(List.of("game", "computer"))
                .build();

        memberRepository.save(member);
    }

    @Test
    void sendMail_새로운이메일_메일발송성공() throws MessagingException {
        // given
        String newEmail = "newuser@example.com";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        doReturn(mimeMessage).when(javaMailSender).createMimeMessage();

        // when
        MailCertificationResponse response = mailService.sendMailJoin(newEmail);

        // then
        assertAll(
                () -> assertThat(response.getEmail()).isEqualTo(newEmail),
                () -> verify(javaMailSender).send(mimeMessage),
                () -> verify(mailCertification).createMailCertification(eq(newEmail), anyString())
        );
    }

    @Test
    void verifyMail_인증성공() {
        // given
        String authNumber = "123456";
        MailCertificationDto requestDto = new MailCertificationDto(testMemberEmail, authNumber);

        doReturn(true).when(mailCertification).hasKey(testMemberEmail);
        doReturn(authNumber).when(mailCertification).getMailCertification(testMemberEmail);

        // when
        Boolean result = mailService.verifyMail(requestDto);

        // then
        assertAll(
                () -> assertThat(result).isTrue(),
                () -> verify(mailCertification).deleteMailCertification(testMemberEmail)
        );
    }

    @Test
    void verifyMail_인증번호불일치_예외발생() {
        // given
        String correctAuthNumber = "123456";
        String incorrectAuthNumber = "654321";
        MailCertificationDto requestDto = new MailCertificationDto(testMemberEmail, incorrectAuthNumber);

        doReturn(true).when(mailCertification).hasKey(testMemberEmail);
        doReturn(correctAuthNumber).when(mailCertification).getMailCertification(testMemberEmail);

        // then
        assertThatThrownBy(() -> mailService.verifyMail(requestDto))
                .isInstanceOf(CertificationNumberMismatchException.class);

        verify(mailCertification, never()).deleteMailCertification(testMemberEmail);
    }

    @Test
    void confirmDupEmail_이메일중복확인_존재하는이메일() {
        // given
        MemberEmailDto memberEmailDto = new MemberEmailDto(testMemberEmail);

        // when
        Boolean isDup = mailService.confirmDupEmail(memberEmailDto);

        // then
        assertThat(isDup).isFalse(); // 이미 존재하므로 중복됨
    }

    @Test
    void confirmDupEmail_이메일중복확인_없는이메일() {
        // given
        String newEmail = "newuser@example.com";
        MemberEmailDto memberEmailDto = new MemberEmailDto(newEmail);

        // when
        Boolean isDup = mailService.confirmDupEmail(memberEmailDto);

        // then
        assertThat(isDup).isTrue(); // 존재하지 않으므로 중복 아님
    }
}