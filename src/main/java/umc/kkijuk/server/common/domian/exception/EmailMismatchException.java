package umc.kkijuk.server.common.domian.exception;
public class EmailMismatchException extends RuntimeException {
    public EmailMismatchException() {
        super("등록된 이메일과 다릅니다. 다시 입력해 주세요.");
    }
}