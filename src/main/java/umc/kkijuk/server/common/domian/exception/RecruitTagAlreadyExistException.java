package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class RecruitTagAlreadyExistException extends RuntimeException {
    public RecruitTagAlreadyExistException(String tag) {
        super("RecruitTag [" + tag + "] already exists");
    }
}
