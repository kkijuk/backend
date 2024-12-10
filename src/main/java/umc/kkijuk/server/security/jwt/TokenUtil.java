//package umc.kkijuk.server.security.jwt;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import umc.kkijuk.server.security.util.CookieUtil;
//
//public class TokenUtil {
//    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
//    private static final int COOKIE_EXPIRE_SECONDS = 60 * 60 * 24 * 365 * 10; // 10년
//
//    // Refresh Token을 쿠키에 저장
//    public static void saveRefreshToken(HttpServletResponse response, String refreshToken) {
//        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, COOKIE_EXPIRE_SECONDS);
//    }
//
//    // Refresh Token을 쿠키에서 가져오기
//    public static String getRefreshToken(HttpServletRequest request) {
//        return CookieUtil.getCookieValue(request, REFRESH_TOKEN_COOKIE_NAME);
//    }
//
//    public static void updateRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response, String newRefreshToken) {
//        CookieUtil.deleteCookie(request, response, "refresh_token");
//        CookieUtil.addCookie(response, "refresh_token", newRefreshToken, COOKIE_EXPIRE_SECONDS);
//    }
//}