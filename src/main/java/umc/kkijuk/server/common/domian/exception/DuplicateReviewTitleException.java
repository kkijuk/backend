package umc.kkijuk.server.common.domian.exception;

import umc.kkijuk.server.common.SkipDiscordNotification;

@SkipDiscordNotification
public class DuplicateReviewTitleException extends RuntimeException{
    public DuplicateReviewTitleException(String title) {
        super("이미 존재하는 리뷰 제목입니다. title: " + title);
    }
}
