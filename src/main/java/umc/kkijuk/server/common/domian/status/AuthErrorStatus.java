package umc.kkijuk.server.common.domian.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorStatus {
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    TOKEN_IS_NULL("토큰값이 null 입니다."),
    EXPIRED_TOKEN("만료된 토큰입니다."),
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),
    LOGOUT_FAILED("로그아웃 실패 - 유효하지 않은 토큰입니다."),
    REDIS_OPERATION_FAILED("Redis 작업 중 오류가 발생했습니다.");

    private final String message;
}
