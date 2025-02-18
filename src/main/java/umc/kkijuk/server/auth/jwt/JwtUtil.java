package umc.kkijuk.server.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Slf4j
@Component
public class JwtUtil {

  @Value("${spring.jwt.secret}")
  private String secretKey;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String createAccessToken(String socialId, boolean isProfileComplete) {
    Date expiration = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
    return Jwts.builder()
            .setId(String.valueOf(socialId))
            .setIssuedAt(new Date())
            .setExpiration(expiration)
            .claim("isProfileComplete",isProfileComplete)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public String createRefreshToken(String socialId, String tokenId) {
    Date expiration = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
    return Jwts.builder()
            .setSubject(String.valueOf(socialId))
            .setId(tokenId)
            .setIssuedAt(new Date())
            .setExpiration(expiration)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.warn("JWT 검증 실패 - 만료된 토큰: {}", e.getMessage());
      return false;
    } catch (SignatureException e) {
      log.warn("JWT 검증 실패 - 서명 불일치: {}", e.getMessage());
      return false;
    } catch (MalformedJwtException e) {
      log.warn("JWT 검증 실패 - 잘못된 형식의 토큰: {}", e.getMessage());
      return false;
    } catch (JwtException e) {
      log.warn("JWT 검증 실패 - 기타 오류: {}", e.getMessage());
      return false;
    }
  }

  public String extractId(String token) {
    return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getId();
  }
  public String extractSubject(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }

}
