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
import umc.kkijuk.server.introduce.controller.response.IntroduceListResponse;
import umc.kkijuk.server.introduce.controller.response.IntroduceResponse;
import umc.kkijuk.server.introduce.dto.*;
import umc.kkijuk.server.introduce.service.IntroduceService;
import umc.kkijuk.server.introduce.service.MasterIntroduceService;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;

import java.util.List;
import java.util.Map;

@Tag(name = "introduce", description = "자기소개서 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/history/intro/")
public class IntroduceController {
    private final IntroduceService introduceService;
    private final MasterIntroduceService masterIntroduceService;
    private final MemberService memberService;

    @PostMapping("/{recruitId}")
    @Operation(summary = "자기소개서 생성")
    public ResponseEntity<Object> save(
            @PathVariable("recruitId") Long recruitId, @RequestBody IntroduceReqDto introduceReqDto){
        LoginUser loginUser = LoginUser.get();
        Member requestMember = memberService.getById(loginUser.getId());
        IntroduceResponse introduceResponse = introduceService.saveIntro(requestMember, recruitId, introduceReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자기소개서 생성 완료", introduceResponse));
    }

    @GetMapping("detail/{introId}")
    @Operation(summary = "자기소개서 개별 조회")
    public ResponseEntity<Object> get(
            @PathVariable("introId") Long introId){
        LoginUser loginUser = LoginUser.get();
        Member requestMember = memberService.getById(loginUser.getId());
        IntroduceResponse introduceResponse = introduceService.getIntro(requestMember, introId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자기소개서 조회 완료", introduceResponse));
    }

    @GetMapping("list")
    @Operation(summary = "자기소개서 목록 조회")
    public ResponseEntity<Object> getList(){
        LoginUser loginUser = LoginUser.get();
        Member requestMember = memberService.getById(loginUser.getId());
        List<IntroduceListResponse> introduceListResponses = introduceService.getIntroList(requestMember);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자기소개서 목록 조회 완료", introduceListResponses));
    }

    @PatchMapping("/{introId}")
    @Operation(summary = "자기소개서 수정")
    public ResponseEntity<Object> update(
            @PathVariable("introId") Long introId, @RequestBody IntroduceReqDto introduceReqDto) throws Exception {
        LoginUser loginUser = LoginUser.get();
        Member requestMember = memberService.getById(loginUser.getId());
        IntroduceResponse introduceResponse = introduceService.updateIntro(requestMember, introId, introduceReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자기소개서 수정 완료", introduceResponse));
    }

    @DeleteMapping("/{introId}")
    @Operation(summary = "자기소개서 삭제")
    public ResponseEntity<Object> delete(
            @PathVariable("introId") Long introId){
        LoginUser loginUser = LoginUser.get();
        Member requestMember = memberService.getById(loginUser.getId());
        Long intro_Id = introduceService.deleteIntro(requestMember, introId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(HttpStatus.OK.value(), "자기소개서 삭제 완료", intro_Id));
    }

    @GetMapping("/search")
    @Operation(summary = "키워드로 자기소개서 문단 검색")
    public ResponseEntity<Map<String, Object>> searchIntroduceByKeyword(@RequestParam String keyword) {
        LoginUser loginUser = LoginUser.get();
        Member requestMember = memberService.getById(loginUser.getId());
        Map<String, Object> response = introduceService.searchIntroduceAndMasterByKeyword(keyword, requestMember);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
