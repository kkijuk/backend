package umc.kkijuk.server.auth.handler;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(
          HttpServletRequest request, HttpServletResponse response, Authentication authentication)
          throws IOException {
    // 기존 리다이렉트 동작 제거
    clearAuthenticationAttributes(request);

    // JSON 응답
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    // 인가코드 반환 (테스트용 코드, 프론트엔드에서 처리 가능)
    String authorizationCode = request.getParameter("code");
    log.info("로그인 성공, 인가코드 반환: {}", authorizationCode);

    response.getWriter().write(String.format(
            "{\"authorizationCode\": \"%s\"}", authorizationCode));
  }
}
