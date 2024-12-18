package umc.kkijuk.server.common;

import lombok.Getter;

@Getter
public class LoginUser {

    private Long id;
    private static final LoginUser LOGIN_USER = new LoginUser(1L);

    public LoginUser(Long id) {
        this.id = id;
    }

    public static LoginUser get() {
        return LOGIN_USER;
    }


}
