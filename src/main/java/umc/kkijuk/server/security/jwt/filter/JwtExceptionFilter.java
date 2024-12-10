//package umc.kkijuk.server.security.jwt.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.JwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import umc.kkijuk.server.security.jwt.response.JwtExceptionResponse;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//
//@Component
//public class JwtExceptionFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            filterChain.doFilter(request, response);    //현재 필터에서의 작업이 끝난 후 다음 필터로 HTTP 요청 전달.(JwtAuthenticationFilter로 이동)
//        } catch (JwtException e) {
//            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
//        }
//    }
//
//    private void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable throwable) throws IOException {
//        response.setStatus(status.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("UTF-8");
//
//        JwtExceptionResponse jwtExceptionResponse = new JwtExceptionResponse(
//                LocalDateTime.now().toString(),
//                HttpStatus.UNAUTHORIZED.value(),
//                "Unauthorized",
//                throwable.getMessage()
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        //JwtExceptionResponse 객체를 JSON 문자열로 변환
//        String jsonResponse = objectMapper.writeValueAsString(jwtExceptionResponse);
//        //문자열을 HTTP 응답으로 전송
//        response.getWriter().write(jsonResponse);
//    }
//}