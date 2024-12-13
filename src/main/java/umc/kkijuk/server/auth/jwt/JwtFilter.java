package umc.kkijuk.server.auth.jwt;

import java.io.IOException;
import java.util.Collections;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.repository.MemberRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final MemberRepository memberRepository;

  @Override
  protected void doFilterInternal(
          HttpServletRequest request, HttpServletResponse response, FilterChain chain)
          throws ServletException, IOException {

    String requestUri = request.getRequestURI();

    // 카카오 로그인 경로 제외
    if (requestUri.startsWith("/auth/kakao/login")) {
      chain.doFilter(request, response);
      return;
    }

    try {
      final String authorizationHeader = request.getHeader("Authorization");
      String email = null;
      String jwt = null;

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
        email = jwtUtil.extractEmail(jwt);
      }

      if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        Member member = memberRepository.findByEmail(email).orElse(null);

        if (member != null && jwtUtil.validateToken(jwt, member.getEmail())) {
          UserDetails userDetails =
                  new org.springframework.security.core.userdetails.User(
                          member.getEmail(),
                          "",
                          Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name())));

          UsernamePasswordAuthenticationToken authentication =
                  new UsernamePasswordAuthenticationToken(
                          userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          log.warn("Invalid JWT Token for member: {}", email);
        }
      }
    } catch (Exception e) {
      log.warn("JWT Authentication Error: {}", e.getMessage());
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
      return;
    }

    chain.doFilter(request, response);
  }
}
