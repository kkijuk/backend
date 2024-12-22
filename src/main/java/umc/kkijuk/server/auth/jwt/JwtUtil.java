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

  public String createAccessToken(String socialId) {
    Date expiration = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // 1시간 유효
    return Jwts.builder()
            .setId(String.valueOf(socialId))
            .setIssuedAt(new Date())
            .setExpiration(expiration)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public String createRefreshToken(String socialId) {
    Date expiration = Date.from(Instant.now().plus(7, ChronoUnit.DAYS)); // 7일 유효
    return Jwts.builder()
            .setId(String.valueOf(socialId))
            .setIssuedAt(new Date())
            .setExpiration(expiration)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public boolean validateToken(String token, String socialId) {
    try {
      Claims claims =
              Jwts.parserBuilder()
                      .setSigningKey(getSigningKey())
                      .build()
                      .parseClaimsJws(token)
                      .getBody();

      String extractedSocialId = claims.getId();
      if (!extractedSocialId.equals(socialId)) {
        log.warn("JWT Token validation failed: socialId mismatch");
        return false;
      }

      if (claims.getExpiration().before(new Date())) {
        log.warn("JWT Token validation failed: Token expired");
        return false;
      }

      return true;
    } catch (JwtException e) {
      log.warn("JWT validation failed: {}", e.getMessage());
      return false;
    }
  }

  public String extractSocialId(String token) {
    return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getId();
  }

}
