package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class FieldUpdateException extends RuntimeException{
    public FieldUpdateException() {super("Field update fail.");
    }
}
