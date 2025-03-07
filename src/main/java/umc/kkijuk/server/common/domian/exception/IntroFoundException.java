package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class IntroFoundException extends RuntimeException {
    public IntroFoundException(String message) {
        super(message);
    }
}
