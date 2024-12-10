//package umc.kkijuk.server.security.jwt;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import umc.kkijuk.server.security.jwt.dto.TokenDto;
//
//import java.security.Key;
//import java.util.Collections;
//import java.util.Date;
//
//@Slf4j
//@Component
//public class TokenProvider {
//    private static final String BEARER_TYPE = "Bearer";
//
//    @Value("${jwt.expiration.access}") //access token 만료 시간
//    private Long accessTokenExpiration;
//
//    //refresh token 만료 시간
//    @Value("${jwt.expiration.refresh}")
//    private Long refreshTokenExpiration;
//
//    private final Key key;  //jwt의 토큰 서명을 생성하고 검증하는데 사용
//
//    public TokenProvider(@Value("${jwt.secret}") String secretKey) {    //secret key 생성
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public TokenDto generateToken(Long userId) {
//        String accessToken = generateAccessToken(userId);
//        String refreshToken = generateRefreshToken();
//
//        return TokenDto.builder()
//                .grantType(BEARER_TYPE)
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//    //Access Token 만료 시 사용
//    public TokenDto reissue(Long userId) {
//        String newAccessToken = generateAccessToken(userId);
//        String newRefreshToken = generateRefreshToken();
//
//        return TokenDto.builder()
//                .grantType(BEARER_TYPE)
//                .accessToken(newAccessToken)
//                .refreshToken(newRefreshToken)
//                .build();
//    }
//
//    public String generateAccessToken(Long userId) {
//        Date now = new Date();
//        Date accessTokenExpireTime = new Date(now.getTime() + accessTokenExpiration);
//
//        //Payload에 사용자를 찾을 수 있게 정보와 권한이 저장되어야 한다.
//        return Jwts.builder()
//                .setSubject(String.valueOf(userId))
//                .setExpiration(accessTokenExpireTime)
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact(); //컴팩트화 -> JWT를 문자열로 반환하는 역할
//    }
//
//    //refresh token은 access token과 다르게 재발급을 위한 것이므로 중요 정보(claim) 없이 만료 시간만 담아도 된다.
//    public String generateRefreshToken() {
//        Date now = new Date();
//        Date refreshTokenExpireTime = new Date(now.getTime() + refreshTokenExpiration);
//
//        return Jwts.builder()
//                .setExpiration(refreshTokenExpireTime)
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact(); //컴팩트화 -> JWT를 문자열로 반환하는 역할
//    }
//
//    public Authentication getAuthentication(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        Long userId = Long.parseLong(claims.getSubject());
//
//        return new UsernamePasswordAuthenticationToken(userId, "", Collections.emptyList());
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            //setSigningKey() -> JWT의 서명 확인 시 필요한 key 설정
//            //parseClaimsJws() -> JWT 토큰을 분석, 확인
//            Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token);
//
//            return true;
//        } catch (UnsupportedJwtException | MalformedJwtException exception) {
//            log.info("유효하지 않은 JWT 토큰");
//        } catch (ExpiredJwtException exception) {
//            log.info("만료된 JWT 토큰입니다.");
//        } catch (IllegalArgumentException exception) {
//            log.info("JWT 토큰 값이 들어있지 않습니다.");
//        } catch (SignatureException exception) {
//            log.info("JWT 토큰 서명이 유효하지 않습니다.");
//        }
//        return false;
//    }
//}
