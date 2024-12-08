package umc.kkijuk.server.recruit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;
import umc.kkijuk.server.recruit.controller.port.RecruitSearchService;
import umc.kkijuk.server.recruit.controller.response.RecruitReviewListByKeywordResponse;
import umc.kkijuk.server.common.LoginUser;

@Tag(name = "Search Recruit", description = "공고 검색 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/recruit")
public class RecruitSearchController {

    private final RecruitSearchService recruitSearchService;
    private final MemberService memberService;

    @Operation(
            summary = "공고 이름 & 태그 / 공고 후기 기준으로 검색",
            description = "입력받은 텍스트에 대하여, 공고 제목, 공고 태그, 혹은 공고 후기에 포함하는 모든 공고를 검색합니다. 정렬기준은 최신순입니다.")
    @GetMapping
    public ResponseEntity<RecruitReviewListByKeywordResponse> findRecruitsByKeyword(
            @RequestParam String keyword) {

        Long loginUser = LoginUser.get().getId();
        Member requestMember = memberService.getById(loginUser);
        RecruitReviewListByKeywordResponse result = recruitSearchService.findRecruitByKeyword(requestMember, keyword);

        return ResponseEntity
                .ok()
                .body(result);
    }
}
