package umc.kkijuk.server.common;

import lombok.Getter;
import umc.kkijuk.server.auth.jwt.JwtUtil;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;

@Getter
public class LoginUser {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private Long id;

    public LoginUser(JwtUtil jwtUtil, MemberService memberService, Long id) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.id = id;
    }

    private static final LoginUser LOGIN_USER = new LoginUser( null, null,1L);


    public static LoginUser get() {
        return LOGIN_USER;
    }

    public Long extractMemberId(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더에 올바른 토큰이 없습니다.");
        }

        String token = bearerToken.substring(7);
        Long socialId = jwtUtil.extractSocialId(token);

        if (socialId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Member member = memberService.findBySocialId(socialId);
        return member.getId();
    }



}
