package umc.kkijuk.server.common.domian.exception;

public class CertificationNumberMismatchException extends RuntimeException{
    public CertificationNumberMismatchException() {super("인증번호가 맞지 않습니다.");
    }
}
