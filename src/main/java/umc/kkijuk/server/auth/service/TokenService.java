package umc.kkijuk.server.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import umc.kkijuk.server.auth.dto.AuthResponse;
import umc.kkijuk.server.auth.jwt.JwtUtil;
import umc.kkijuk.server.common.domian.exception.CustomAuthException;
import umc.kkijuk.server.common.domian.status.AuthErrorStatus;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.repository.MemberRepository;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "REFRESH:";
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;

    public AuthResponse rotateRefreshToken(String refreshToken) {
        Map<String, String> tokenInfo = validateAndExtractSocialId(refreshToken);
        String socialId = tokenInfo.get("socialId");

        Member member = memberRepository.findBySocialId(socialId)
                .orElseThrow(()->{
                    log.error("회원 조회 실패 - 존재하지 않는 회원 socialId: {}",socialId);
                    return new CustomAuthException(AuthErrorStatus.USER_NOT_FOUND);

                });

        boolean isProfileComplete = member.getIsProfileComplete();
        String newTokenId = UUID.randomUUID().toString();
        Map<String, String> newTokens = createNewTokens(socialId, isProfileComplete, newTokenId);
        String newRefreshTokenKey = REFRESH_TOKEN_PREFIX + socialId + ":" + newTokenId;

        redisTemplate
                .opsForValue()
                .set(
                        newRefreshTokenKey,
                        newTokens.get("refreshToken"),
                        REFRESH_TOKEN_EXPIRE_TIME,
                        TimeUnit.MILLISECONDS);

        log.info(" Refresh Token 갱신 완료: 전{} -> 후{}", refreshToken, newTokens.get("refreshToken"));
        return AuthResponse.builder().accessToken(newTokens.get("accessToken")).refreshToken(newTokens.get("refreshToken")).build();

    }

    public void logout(String refreshToken) {
        if (refreshToken == null) {
            log.error("로그아웃 요청 실패 - refreshToken이 null 임");
            throw new CustomAuthException(AuthErrorStatus.TOKEN_IS_NULL);
        }
        if(!jwtUtil.validateToken(refreshToken)){
            log.error(" 로그아웃 실패 - 유효하지 않은 Refresh Token");
            throw new CustomAuthException(AuthErrorStatus.INVALID_TOKEN);
        }
        String socialId = jwtUtil.extractSubject(refreshToken);
        String tokenId = jwtUtil.extractId(refreshToken);

        String refreshTokenKey = REFRESH_TOKEN_PREFIX + socialId + ":" + tokenId;
        redisTemplate.delete(refreshTokenKey);
        log.info("로그아웃 - Refresh Token 삭제 완료  {}", refreshTokenKey);

    }

    public Map<String, String> validateAndExtractSocialId(String refreshToken) {
        if (refreshToken == null) {
            log.error("토큰 검증 실패 - refreshToken이 null 임");
            throw new CustomAuthException(AuthErrorStatus.TOKEN_IS_NULL);
        }
        jwtUtil.validateToken(refreshToken);

        String socialId = jwtUtil.extractSubject(refreshToken);
        String tokenId = jwtUtil.extractId(refreshToken);

        String refreshTokenKey = REFRESH_TOKEN_PREFIX + socialId + ":" + tokenId;
        String storedToken = redisTemplate.opsForValue().get(refreshTokenKey);

        if (storedToken == null) {
            log.error("토큰 검증 실패 - 만료된 Refresh Token: {}", refreshToken);
            throw new CustomAuthException(AuthErrorStatus.EXPIRED_TOKEN );
        }
        redisTemplate.delete(refreshTokenKey);
        log.info("RefreshToken 삭제 완료 : {}", refreshToken);

        return Map.of(
                "socialId",socialId,
                "tokenId",tokenId
        );

    }
    public Map<String, String> createNewTokens(
            String socialId, boolean isProfileComplete, String tokenId) {
        String newAccessToken = jwtUtil.createAccessToken(socialId, isProfileComplete);
        String newRefreshToken = jwtUtil.createRefreshToken(socialId, tokenId);

        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken);
    }


    public void invalidateRefreshToken(String socialId) {
        try {
            Set<String> keys = redisTemplate.keys(REFRESH_TOKEN_PREFIX + socialId + ":*");

            if (keys == null || keys.isEmpty()) {
                log.warn("Refresh Token 삭제 실패 - 해당 Social ID({})에 대한 토큰이 없음", socialId);
                return;
            }

            keys.forEach(redisTemplate::delete);
            log.info("회원 비활성화 - Refresh Token 삭제 완료: {}", keys);
        } catch (Exception e) {
            log.error("Refresh Token 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomAuthException(AuthErrorStatus.REDIS_OPERATION_FAILED); // ✅ 필요하면 예외 정의하여 던지기
        }
    }
}
