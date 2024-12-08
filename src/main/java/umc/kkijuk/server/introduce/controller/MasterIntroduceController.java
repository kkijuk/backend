package umc.kkijuk.server.introduce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.common.LoginUser;
import umc.kkijuk.server.introduce.common.BaseResponse;
import umc.kkijuk.server.introduce.controller.response.IntroduceResponse;
import umc.kkijuk.server.introduce.controller.response.MasterIntroduceResponse;
import umc.kkijuk.server.introduce.dto.IntroduceReqDto;
import umc.kkijuk.server.introduce.dto.MasterIntroduceReqDto;
import umc.kkijuk.server.introduce.service.MasterIntroduceService;
import umc.kkijuk.server.login.argumentresolver.Login;
import umc.kkijuk.server.login.controller.dto.LoginInfo;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;


@Tag(name = "master", description = "마스터 자기소개서 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/history/intro/master")
public class MasterIntroduceController {
    private final MasterIntroduceService masterIntroduceService;
    private final MemberService memberService;

    private final Member requestMember = Member.builder()
            .id(LoginUser.get().getId())
            .build();

    @PostMapping
    @Operation(summary = "마스터 자기소개서 생성")
    public ResponseEntity<Object> saveMasterIntro(@RequestBody IntroduceReqDto introduceReqDto) throws Exception {
        LoginUser loginUser = LoginUser.get();
        MasterIntroduceResponse masterIntroduceResponse =
                masterIntroduceService.saveMasterIntro(loginUser.getId(), introduceReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "마스터 자기소개서 생성 완료", masterIntroduceResponse));
    }

    @GetMapping
    @Operation(summary = "마스터 자기소개서 조회")
    public ResponseEntity<Object> getMasterIntro(){
        LoginUser loginUser = LoginUser.get();
        MasterIntroduceResponse masterIntroduceResponse = masterIntroduceService.getMasterIntro(loginUser.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "마스터 자기소개서 조회 완료", masterIntroduceResponse));
    }

    @PatchMapping
    @Operation(summary = "마스터 자기소개서 수정")
    public ResponseEntity<Object> updateMasterIntro(
            @RequestBody IntroduceReqDto introduceReqDto) throws Exception {
        LoginUser loginUser = LoginUser.get();
        MasterIntroduceResponse masterIntroduceResponse = masterIntroduceService.updateMasterIntro(loginUser.getId(), introduceReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "마스터 자기소개서 수정 완료", masterIntroduceResponse));
    }

}
