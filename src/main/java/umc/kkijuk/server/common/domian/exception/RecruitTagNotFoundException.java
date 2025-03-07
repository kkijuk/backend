package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class RecruitTagNotFoundException extends RuntimeException {
    public RecruitTagNotFoundException(String tag) {
        super("RecruitTag [" + tag + "] not found");
    }
}
