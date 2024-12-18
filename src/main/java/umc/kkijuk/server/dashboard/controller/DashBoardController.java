package umc.kkijuk.server.dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.kkijuk.server.common.LoginUser;
import umc.kkijuk.server.dashboard.controller.port.DashBoardService;
import umc.kkijuk.server.dashboard.controller.response.DashBoardUserInfoResponse;
import umc.kkijuk.server.dashboard.controller.response.IntroduceRemindResponse;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;
import umc.kkijuk.server.dashboard.controller.response.RecruitRemindResponse;

import java.util.List;

@Tag(name = "dashboard", description = "메인화면 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashBoardController {
    private final DashBoardService dashBoardService;
    private final MemberService memberService;
    private final LoginUser loginUser;

//    private final Member requestMember = Member.builder()
//            .id(LoginUser.get().getId())
//            .name("tester")
//            .build();

    @Operation(
            summary = "메인화면 정보 보드",
            description = "메인화면에 사용자이름, 가입한 기간, 활동, 지원현황 갯수 데이터를 응답합니다.")
    @GetMapping("/user-info")
    public ResponseEntity<DashBoardUserInfoResponse> getUserInfo(
            @RequestHeader("Authorization") String token
            ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        DashBoardUserInfoResponse response = dashBoardService.getUserInfo(requestMember);
        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(
            summary = "메인화면 공고 리마인더 API",
            description = "생성한 공고 중 남은 마감일자가 적은 순서로 두 개 출력"
    )
    @GetMapping("/remind/recruit")
    public ResponseEntity<RecruitRemindResponse> getRemindRecruits(
            @RequestHeader("Authorization") String token
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        RecruitRemindResponse response = dashBoardService.getTopTwoRecruitsByEndTime(requestMember);
        return ResponseEntity
                .ok()
                .body(response);
    }

    @GetMapping("/introduce")
    @Operation(summary = "홈 자기소개서 작성 알림")
    public ResponseEntity<Object> get(@RequestHeader("Authorization") String token
    ){
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        List<IntroduceRemindResponse> homeIntroduceResDtos = dashBoardService.getHomeIntro(requestMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(homeIntroduceResDtos);
    }
}
