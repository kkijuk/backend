package umc.kkijuk.server.career.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.career.controller.response.*;
import umc.kkijuk.server.career.service.CareerSearchService;
import umc.kkijuk.server.common.LoginUser;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;

import java.util.List;

@Tag(name="Search Career", description = "활동 조회 및 검색 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/career")
public class CareerSearchController {
    private final CareerSearchService careerSearchService;
    private final MemberService memberService;
    private final LoginUser loginUser;
    @GetMapping("")
    @Operation(
            summary = "활동 목록",
            description = "활동을 조회합니다. query 값으로 category(카테고리 기준), year(연도 기준), 또는 all(전체 조회) 중 하나를 선택하여 요청해주세요." )
    public CareerResponse<?> findAllCareersGroupedYear(
            @RequestHeader("Authorization") String token,
            @RequestParam(name="status") String value
    ) {
        Member requestMember = loginUser.extractMemberId(token);
        if(value.equals("category")){
            return CareerResponse.success(
                    CareerResponseMessage.CAREER_FINDALL_SUCCESS,
                    careerSearchService.findAllCareerGroupedCategory(requestMember.getId())
            );
        }else if (value.equals("all")){
            return CareerResponse.success(
                    CareerResponseMessage.CAREER_FINDALL_SUCCESS,
                    careerSearchService.findAllCareer(requestMember.getId())
            );
        }
        return CareerResponse.success(
                CareerResponseMessage.CAREER_FINDALL_SUCCESS,
                careerSearchService.findAllCareerGroupedYear(requestMember.getId())
        );
    }
    @GetMapping("/{type}/{careerId}")
    @Operation(summary = "활동 상세", description = "활동 ID에 해당하는 활동의 세부 내용과, 활동 기록을 조회합니다.")
    @Parameter(name = "careerId", description = "활동 Id, path variable 입니다.", example = "1")
    public CareerResponse<BaseCareerResponse> findCareer(
            @RequestHeader("Authorization") String token,
            @PathVariable String type,
            @PathVariable Long careerId
    ){
        Member requestMember = loginUser.extractMemberId(token);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_FINDALL_SUCCESS,
                careerSearchService.findCareer(requestMember, careerId, type)
        );
    }
    @GetMapping("/find/detail")
    @Operation(
            summary = "활동 검색 - 활동 기록",
            description = "활동기록을 주어진 조건에 맞추어 조회합니다. query 값으로 검색어(keyword)와 정렬 기준(new,old)을 요청해주세요. " )
    public CareerResponse<List<FindDetailResponse>> findDetail(
            @RequestHeader("Authorization") String token,
            @RequestParam(name="keyword")String keyword,
            @RequestParam(name="sort") String sort
    ) {
        Member requestMember = loginUser.extractMemberId(token);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_FINDALL_SUCCESS,
                careerSearchService.findAllDetail(requestMember,keyword,sort)
        );
    }

    @GetMapping("/find/taglist")
    @Operation(
            summary = "활동 검색 - 태그 ( 검색 태그 조회 )",
            description = "검색어를 포함하는 활동 태그들을 가나다 순으로 조회합니다.  " +
                    "query 값으로 검색어(keyword)를 요청해주세요. " )
    public CareerResponse<FindTagResponse.SearchTagResponse> findTag(
            @RequestHeader("Authorization") String token,
            @RequestParam(name="keyword")String keyword
    ) {
        Member requestMember = loginUser.extractMemberId(token);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_SEARCH_SUCCESS,
                careerSearchService.findAllTag(requestMember, keyword)
        );
    }

    @GetMapping("/find/tag")
    @Operation(
            summary = "활동 검색 - 태그 ( 선택한 태그에 대한 활동 기록 조회 )",
            description = "선택한 태그를 포함하는 활동 기록들을 조회합니다. " +
                    " query 값으로 태그의 ID 와 정렬 기준(new,old)을 요청해주세요. " )
    public CareerResponse<List<FindDetailResponse>> findTagAndDetail(
            @RequestHeader("Authorization") String token,
            @RequestParam(name="tagId") Long tagId,
            @RequestParam(name="sort") String sort
    ){
        Member requestMember = loginUser.extractMemberId(token);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_FINDALL_SUCCESS,
                careerSearchService.findAllDetailByTag(requestMember, tagId, sort)
        );
    }
    @GetMapping("/find")
    @Operation(
            summary = "활동 검색 - 활동",
            description =  "활동을 주어진 조건에 맞추어 조회합니다. query 값으로 검색어(keyword)와 정렬 기준(new,old)을 요청해주세요. " )
    public CareerResponse<List<FindCareerResponse>> findCareerWithKeyword(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "sort") String sort
    ){
        Member requestMember = loginUser.extractMemberId(token);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_FINDALL_SUCCESS,
                careerSearchService.findCareerWithKeyword(requestMember, keyword, sort)
        );

    }
    @GetMapping("/timeline")
    @Operation(
            summary = "활동 타임라인",
            description = "타임라인에 필요한 활동 정보들을 조회합니다.")
    public CareerResponse<List<TimelineResponse>> findCareerForTimeline(
            @RequestHeader("Authorization") String token
    ){
        Member requestMember = loginUser.extractMemberId(token);
        return CareerResponse.success(
                CareerResponseMessage.CAREER_FINDALL_SUCCESS,
                careerSearchService.findCareerForTimeline(requestMember)
        );

    }

}
