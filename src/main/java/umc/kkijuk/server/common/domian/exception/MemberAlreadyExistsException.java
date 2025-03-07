package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class MemberAlreadyExistsException extends RuntimeException{
    public MemberAlreadyExistsException() {super("Member Already Exists");
    }
}
