package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class IntroOwnerMismatchException extends RuntimeException {
    public IntroOwnerMismatchException() {
        super("해당 문서에 대한 소유권을 가지고 있지 않습니다.");
    }
}
