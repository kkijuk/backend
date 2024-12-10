//package umc.kkijuk.server.security.jwt.filter;
//
///*
//HTTP 요청을 중간에서 가로채서 jwt 처리, 해당 토큰으로 사용자 인증
//jwt 토큰 추출 -> 유효성 검사 -> if 유효: 토큰으로 사용자 인증 후 SecurityContextHolder에 인증 정보 설정
// */
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//import umc.kkijuk.server.security.jwt.TokenProvider;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthorizationFilter extends OncePerRequestFilter {  //커스텀 필터 클래스
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//    private static final String BEARER_PREFIX = "Bearer ";
//    private final TokenProvider tokenProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String jwt = resolveToken(request);
//
//        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//            Authentication authentication = tokenProvider.getAuthentication(jwt);   //토큰 사용하여 사용자 인증
//            SecurityContextHolder.getContext().setAuthentication(authentication);   //SecurityContextHolder에 인증 정보 설정
//        }
//
//        filterChain.doFilter(request, response);    //이 필터의 작업이 끝난 후 다음 필터로 http 요청 전달
//    }
//
//    private String resolveToken(HttpServletRequest request) {   //여기서 HttpServeletRequest는 HTTP 요청 정보를 캡슐화한 객체
//        String token = request.getHeader(AUTHORIZATION_HEADER);
//
//        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {    //토큰에서 추출한 헤더 값이 null이 아닌지 && "Bearer "로 시작하는지 -> "Bearer " 다음에 토큰이 오는 것이 관례
//            return token.substring(BEARER_PREFIX.length()); //"Bearer " 제외 후 실제 토큰 문자열을 반환
//        }
//        return null;
//    }
//}
