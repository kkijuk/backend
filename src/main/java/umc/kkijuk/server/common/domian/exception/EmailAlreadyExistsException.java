package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {super("이미 존재하는 이메일입니다.");
    }
}
