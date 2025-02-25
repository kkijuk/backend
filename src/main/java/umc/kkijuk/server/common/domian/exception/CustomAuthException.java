package umc.kkijuk.server.common.domian.exception;

import lombok.Getter;
import umc.kkijuk.server.common.domian.status.AuthErrorStatus;

@Getter
public class CustomAuthException extends RuntimeException {
    private final AuthErrorStatus errorStatus;

    public CustomAuthException(AuthErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;

    }
}
