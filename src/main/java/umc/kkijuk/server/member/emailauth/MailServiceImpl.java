package umc.kkijuk.server.member.emailauth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import umc.kkijuk.server.common.domian.exception.CertificationNumberMismatchException;
import umc.kkijuk.server.common.domian.exception.EmailNotResistedException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.dto.MemberEmailDto;
import umc.kkijuk.server.member.repository.MemberRepository;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final RedisService mailCertification;
    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;


    private String createRandomNumber() {
        Random rand = new Random();
        String randomNum = "";
        for (int i = 0; i < 6; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum += random;
        }

        return randomNum;
    }

    public MailCertificationResponse sendMailPasswordReset(String email) {
        if(memberRepository.findByEmail(email).isEmpty()){
            throw new EmailNotResistedException();
        }

        return this.sendMailJoin(email);
    }

    public MailCertificationResponse sendMailJoin(String email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String randomNum = createRandomNumber();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + randomNum + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(message);
        mailCertification.createMailCertification(email,randomNum);
        return new MailCertificationResponse(email);
    }


    // 인증 번호 검증
    public Boolean verifyMail(MailCertificationDto requestDto) {
        if (isVerify(requestDto)) {
            throw new CertificationNumberMismatchException();
        }
        mailCertification.deleteMailCertification(requestDto.getEmail());

        return true;
    }
    private Boolean isVerify(MailCertificationDto requestDto) {
        return !(mailCertification.hasKey(requestDto.getEmail()) &&
                mailCertification.getMailCertification(requestDto.getEmail())
                        .equals(requestDto.getAuthNumber()));
    }

    @Override
    public Boolean confirmDupEmail(MemberEmailDto memberEmailDto) {
        Optional<Member> member = memberRepository.findByEmail(memberEmailDto.getEmail());
        if(member.isEmpty()){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
}