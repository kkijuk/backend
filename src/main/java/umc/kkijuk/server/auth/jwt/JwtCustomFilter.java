package umc.kkijuk.server.auth.jwt;

import java.io.IOException;

import lombok.RequiredArgsConstructor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtCustomFilter extends OncePerRequestFilter {

  private final JwtFilter jwtFilter;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String requestUri = request.getRequestURI();

    // 특정 요청 URI에 대해 필터 제외
    if (requestUri.matches("^\\/login(?:\\/.*)?$") || requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
      filterChain.doFilter(request, response);
      return;
    }

    // JWT 필터 실행
    jwtFilter.doFilterInternal(request, response, filterChain);
  }
}
