package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class InvalidTagNameException extends RuntimeException{
    public InvalidTagNameException(String message) {
        super(message);

    }
}
