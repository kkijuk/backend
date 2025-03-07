package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class EmailNotResistedException extends RuntimeException{
    public EmailNotResistedException() {super("등록되지 않은 이메일입니다.");
    }
}
