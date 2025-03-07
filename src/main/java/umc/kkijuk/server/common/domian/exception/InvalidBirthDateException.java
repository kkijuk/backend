package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class InvalidBirthDateException extends RuntimeException {
    public InvalidBirthDateException() {
        super("올바른 생년월일을 입력해주세요.");
    }
}