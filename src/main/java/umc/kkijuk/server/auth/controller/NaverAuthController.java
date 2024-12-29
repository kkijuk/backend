package umc.kkijuk.server.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.kkijuk.server.auth.service.NaverAuthService;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.member.repository.MemberRepository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NaverAuthController {
    private final NaverAuthService naverAuthService;
    private final MemberRepository memberRepository;

    @GetMapping("/auth/naver/login")
    public ResponseEntity<Object> naverCallback(@RequestParam("code") String code,
                                                @RequestParam("state") String state){
        try{
            String naverAccessToken = naverAuthService.getNaverAccessToken(code, state);
            Member member = naverAuthService.processNaverUser(naverAccessToken);

            if (member.getUserState().equals(State.INACTIVATE)) {
                member.activate();
                memberRepository.save(member);
            }

            Map<String, Object> tokens = new HashMap<>();
            tokens.put("Token", naverAuthService.generateTokens(member));
            log.info("네이버 로그인 성공: 사용자 이름={}, 네이버 ID={}", member.getName(), member.getSocialId());
            return ResponseEntity.ok(tokens);

        }catch (Exception e){
            log.error("네이버 인증 처리 중 오류 발생 : {}",e.getMessage(),e);
            return ResponseEntity.internalServerError().body(Map.of("error", "네이버 인증 처리 실패"));
        }
    }
}
