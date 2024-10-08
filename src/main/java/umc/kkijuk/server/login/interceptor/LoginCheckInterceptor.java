package umc.kkijuk.server.login.interceptor;

import io.micrometer.common.util.StringUtils;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import umc.kkijuk.server.login.controller.SessionConst;
import umc.kkijuk.server.login.exception.UnauthorizedException;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("{} 인증 필터 동작", requestURI);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER_INFO) == null) {
            log.info("미인증 사용자 요청");

            request.getRequestDispatcher("/api/error").forward(request, response);

            return false;
        }

        return true;
    }
}
