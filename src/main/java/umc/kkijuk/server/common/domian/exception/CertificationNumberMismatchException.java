package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class CertificationNumberMismatchException extends RuntimeException{
    public CertificationNumberMismatchException() {super("인증번호가 맞지 않습니다.");
    }
}
