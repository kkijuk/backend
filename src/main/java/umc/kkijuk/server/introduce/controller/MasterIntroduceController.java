package umc.kkijuk.server.introduce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.common.LoginUser;
import umc.kkijuk.server.introduce.common.BaseResponse;
import umc.kkijuk.server.introduce.controller.response.MasterIntroduceResponse;
import umc.kkijuk.server.introduce.dto.IntroduceReqDto;
import umc.kkijuk.server.introduce.service.MasterIntroduceService;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;


@Tag(name = "master", description = "마스터 자기소개서 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/history/intro/master")
public class MasterIntroduceController {
    private final MasterIntroduceService masterIntroduceService;
    private final LoginUser loginUser;


    @PostMapping
    @Operation(summary = "마스터 자기소개서 생성")
    public ResponseEntity<Object> saveMasterIntro(@RequestHeader("Authorization") String token,
                                                  @RequestBody IntroduceReqDto introduceReqDto) throws Exception {
        Member member = loginUser.extractMemberId(token);
        Long memberId = member.getId();

        MasterIntroduceResponse masterIntroduceResponse =
                masterIntroduceService.saveMasterIntro(memberId, introduceReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "마스터 자기소개서 생성 완료", masterIntroduceResponse));
    }

    @GetMapping
    @Operation(summary = "마스터 자기소개서 조회")
    public ResponseEntity<Object> getMasterIntro(@RequestHeader("Authorization") String token){
        Member member = loginUser.extractMemberId(token);
        Long memberId = member.getId();

        MasterIntroduceResponse masterIntroduceResponse = masterIntroduceService.getMasterIntro(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "마스터 자기소개서 조회 완료", masterIntroduceResponse));
    }

    @PatchMapping
    @Operation(summary = "마스터 자기소개서 수정")
    public ResponseEntity<Object> updateMasterIntro(@RequestHeader("Authorization") String token,
            @RequestBody IntroduceReqDto introduceReqDto) throws Exception {
        Member member = loginUser.extractMemberId(token);
        Long memberId = member.getId();

        MasterIntroduceResponse masterIntroduceResponse = masterIntroduceService.updateMasterIntro(memberId, introduceReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "마스터 자기소개서 수정 완료", masterIntroduceResponse));
    }

}
