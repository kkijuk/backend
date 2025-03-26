package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class FileValidationException extends RuntimeException{
    public FileValidationException(String message) {
        super(message);
    }
}
