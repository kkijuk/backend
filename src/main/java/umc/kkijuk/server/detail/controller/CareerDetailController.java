package umc.kkijuk.server.detail.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.common.LoginUser;
import umc.kkijuk.server.detail.controller.response.BaseCareerDetailResponse;
import umc.kkijuk.server.detail.controller.response.CareerDetailResponse;
import umc.kkijuk.server.detail.dto.CareerDetailReqDto;
import umc.kkijuk.server.detail.dto.CareerDetailUpdateReqDto;
import umc.kkijuk.server.detail.service.BaseCareerDetailService;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;

@Tag(name="careerdetail",description = "내커리어 활동 기록 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/career/detail")
public class CareerDetailController {
    private final MemberService memberService;
    private final BaseCareerDetailService careerDetailService;
    private final LoginUser loginUser;

    @PostMapping("/{careerId}")
    @Operation(summary = "활동 기록 생성", description = "주어진 정보를 바탕으로 활동기록을 생성합니다.")
    @Parameters({
            @Parameter(name = "careerId", description = "활동 Id, path variable 입니다."),
    })
    public CareerDetailResponse<BaseCareerDetailResponse> create(
            @RequestHeader("Authorization") String token,
            @PathVariable Long careerId,
            @RequestBody @Valid CareerDetailReqDto request
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerDetailResponse.success(HttpStatus.CREATED, "활동 기록을 성공적으로 생성했습니다.",
                careerDetailService.createDetail(requestMember, request, careerId)
        );
    }
    @DeleteMapping("/{careerId}/{detailId}")
    @Operation(summary = "활동 기록 삭제", description = "활동 기록 ID에 해당하는 활동을 삭제합니다.")
    @Parameters({
            @Parameter(name = "careerId", description = "활동 Id, path variable 입니다."),
            @Parameter(name = "detailId", description = "활동 기록 Id, path variable 입니다.")
    })
    public CareerDetailResponse<Object> delete(
            @RequestHeader("Authorization") String token,
            @PathVariable Long careerId,
            @PathVariable Long detailId
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        careerDetailService.deleteDetail(requestMember, careerId ,detailId);
        return CareerDetailResponse.success(HttpStatus.OK, "활동 기록을 성공적으로 삭제했습니다.",null);
    }

    @PatchMapping("/{careerId}/{detailId}")
    @Operation(summary = "활동 기록 수정",description = "주어진 정보를 바탕으로 활동 기록 ID에 해당하는 활동을 수정합니다. ")
    @Parameters({
            @Parameter(name = "careerId",description = "활동 Id, path variable 입니다."),
            @Parameter(name = "detailId", description = "활동 기록 Id, path variable 입니다. ")
    })
    public CareerDetailResponse<BaseCareerDetailResponse> update(
            @RequestHeader("Authorization") String token,
            @PathVariable Long careerId,
            @PathVariable Long detailId,
            @RequestBody @Valid CareerDetailUpdateReqDto request
    ){
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        return CareerDetailResponse.success(
                HttpStatus.OK,
                "활동 기록을 성공적으로 수정했습니다.",
                careerDetailService.updateDetail(requestMember, request, careerId ,detailId)
        );

    }

}
