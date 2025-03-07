package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class CareerValidationException extends RuntimeException{

    public CareerValidationException(String message) {
        super(message);
    }
}
