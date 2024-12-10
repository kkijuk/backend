//package umc.kkijuk.server.security.util;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.util.Arrays;
//
//public class CookieUtil {
//    private static final int MAX_LIST_SIZE = 5;
//
//    public static String getCookieValue(HttpServletRequest request, String cookieName) {
//        Cookie cookie = getCookie(request, cookieName);
//        return cookie != null ? cookie.getValue() : null;
//    }
//
//    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int expireSeconds) {
//        Cookie cookie = new Cookie(cookieName, cookieValue);
//        cookie.setPath("/");
//        cookie.setMaxAge(expireSeconds);
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
//        response.addCookie(cookie);
//    }
//
//    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
//        Cookie cookie = getCookie(request, cookieName);
//        if (cookie != null) {
//            cookie.setValue("");
//            cookie.setMaxAge(0);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//        }
//    }
//
//    private static Cookie getCookie(HttpServletRequest request, String cookieName) {
//        if (request.getCookies() != null) {
//            return Arrays.stream(request.getCookies())
//                    .filter(cookie -> cookie.getName().equals(cookieName))
//                    .findFirst()
//                    .orElse(null);
//        }
//        return null;
//    }
//}
