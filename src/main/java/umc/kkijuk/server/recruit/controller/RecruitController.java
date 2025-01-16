package umc.kkijuk.server.recruit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.kkijuk.server.common.LoginUser;
import umc.kkijuk.server.introduce.service.IntroduceService;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.service.MemberService;
import umc.kkijuk.server.recruit.controller.port.RecruitService;
import umc.kkijuk.server.recruit.controller.response.*;
import umc.kkijuk.server.recruit.domain.*;
import umc.kkijuk.server.review.controller.port.ReviewService;
import umc.kkijuk.server.review.domain.Review;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Tag(name = "recruit", description = "모집 공고 API")
@Slf4j
@Builder
@RestController
@RequiredArgsConstructor
@RequestMapping("/recruit")
public class RecruitController {
    private final RecruitService recruitService;
    private final ReviewService reviewService;
    private final MemberService memberService;
    private final LoginUser loginUser;
    private final IntroduceService introduceService;

    @Operation(
            summary = "지원 공고 생성",
            description = "주어진 정보를 바탕으로 지원 공고 데이터를 생성합니다.")
    @PostMapping
    public ResponseEntity<RecruitIdResponse> create(@RequestHeader("Authorization") String token,
            @RequestBody @Valid RecruitCreate recruitCreate
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        Recruit recruit = recruitService.create(requestMember, recruitCreate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RecruitIdResponse.from(recruit));
    }

    @Operation(
            summary = "지원 공고 수정",
            description = "주어진 정보를 바탕으로 지원 공고 데이터를 수정합니다.")
    @Parameter(name = "recruitId", description = "지원 공고 ID", example = "1")
    @PutMapping("/{recruitId}")
    public ResponseEntity<RecruitIdResponse> update(@RequestHeader("Authorization") String token,
            @RequestBody @Valid RecruitUpdate recruitUpdate,
            @PathVariable long recruitId) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        Recruit recruit = recruitService.update(requestMember, recruitId, recruitUpdate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RecruitIdResponse.from(recruit));
    }

    @Operation(
            summary = "지원 공고 상태 수정",
            description = "다음 중 주어진 상태로 지원 공고의 상태를 수정합니다." +  " [UNAPPLIED / PLANNED / APPLYING / REJECTED / ACCEPTED]")
    @Parameter(name = "recruitId", description = "지원 공고 ID", example = "1")
    @PatchMapping("/{recruitId}/status")
    public ResponseEntity<RecruitIdResponse> updateState(@RequestHeader("Authorization") String token,
            @RequestBody @Valid RecruitStatusUpdate recruitStatusUpdate,
            @PathVariable long recruitId) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        Recruit recruit = recruitService.updateStatus(requestMember, recruitId, recruitStatusUpdate);
        return ResponseEntity
                .ok()
                .body(RecruitIdResponse.from(recruit));
    }

    @Operation(
            summary = "지원 공고 삭제",
            description = "지원 공고 ID에 해당 하는 공고를 삭제합니다")
    @Parameter(name = "recruitId", description = "지원 공고 ID", example = "1")
    @DeleteMapping("/{recruitId}")
    public ResponseEntity<RecruitIdResponse> delete(@RequestHeader("Authorization") String token,
            @PathVariable long recruitId) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        Recruit recruit = recruitService.disable(requestMember, recruitId);
        return ResponseEntity
                .ok()
                .body(RecruitIdResponse.from(recruit));
    }

    @Operation(
            summary = "지원 공고 상세",
            description = "지원 공고 ID에 해당하는 공고의 상세 정보를 요청합니다.")
    @Parameter(name = "recruitId", description = "지원 공고 ID", example = "1")
    @GetMapping("/{recruitId}")
    public ResponseEntity<RecruitInfoResponse> getRecruitInfo(@RequestHeader("Authorization") String token,
            @PathVariable long recruitId) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        Recruit recruit = recruitService.getById(recruitId);
        List<Review> reviews = reviewService.findAllByRecruit(requestMember, recruit);
        int state = introduceService.findStateByRecruitId(recruitId);

        return ResponseEntity
                .ok()
                .body(RecruitInfoResponse.from(recruit, reviews, state));
    }

    @Operation(
            summary = "지원 공고 목록 (특정 날짜 이후)",
            description = "주어진 날짜에 마감 종료되는 지원 공고들의 목록을 요청합니다.")
    @GetMapping("/list/end")
    public ResponseEntity<RecruitListByEndDateResponse> findAllRecruitListByEndTime(
            @RequestHeader("Authorization") String token,
            @Parameter(name = "date", description = "지원 공고 마감 날짜", example = "2024-07-20")
            @RequestParam LocalDate date) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        Map<Recruit, String> recruits = recruitService.findAllByEndTime(requestMember, date);
        return ResponseEntity
                .ok()
                .body(RecruitListByEndDateResponse.from(recruits));
    }

    @Operation(
            summary = "지원 공고 목록 (특정 시간 이후)",
            description = "주어진 시간 이후 마감 종료되는 지원 공고들의 목록을 요청합니다.")
    @GetMapping("/list/after")
    public ResponseEntity<RecruitListByEndTimeAfterResponse> findAllRecruitListByEndTimeAfterNow(
            @RequestHeader("Authorization") String token,
            @Parameter(name = "time", description = "이 시간 이후에 마감되는 공고를 요청", example = "2024-07-20 10:30")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
            @RequestParam LocalDateTime time) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        Map<Recruit, String> recruits = recruitService.findAllByEndTimeAfter(requestMember, time);
        return ResponseEntity
                .ok()
                .body(RecruitListByEndTimeAfterResponse.from(recruits));
    }

    @Operation(
            summary = "지원 현황 목록",
            description = "주어진 시간 이후 유효한 지원 목록을 불러옵니다. " +
                    "상태가 UNAPPLIED 혹은 PLANNED일 경우, 해당 지원 공고의 마감시간이 요청한 시간보다 이후여야 합니다.")
    @GetMapping("/list/valid")
    public ResponseEntity<ValidRecruitListResponse> findValidRecruit(
            @RequestHeader("Authorization") String token,
            @Parameter(name = "time", description = "이 시간 이후에 유효한 공고 목록 요청", example = "2024-07-20 10:30")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
            @RequestParam LocalDateTime time
    ) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        List<ValidRecruitDto> ValidRecruitDtoList = recruitService.findAllValidRecruitByMember(requestMember, time);
        return ResponseEntity
                .ok()
                .body(ValidRecruitListResponse.from(ValidRecruitDtoList));
    }

    @Operation(
            summary = "달력",
            description = "요청한 년도와 월에 대해서 해당 월에 마감되는 공고의 종류와 갯수를 요청합니다.")
    @GetMapping("/calendar")
    public ResponseEntity<RecruitListByMonthResponse> getByMonth(
            @RequestHeader("Authorization") String token,
            @Parameter(name = "year", description = "년도", example = "2024")
            @RequestParam Integer year,
            @Parameter(name = "month", description = "월", example = "7")
            @RequestParam Integer month) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        List<RecruitListByMonthDto> recruitListByMonthDtoList = recruitService.findAllValidRecruitByYearAndMonth(requestMember, year, month);
        return ResponseEntity
                .ok()
                .body(RecruitListByMonthResponse.from(recruitListByMonthDtoList));
    }

    @Operation(
            summary = "공고 지원 날짜 수정",
            description = "공고 지원 날짜를 주어진 날짜로 수정합니다.")
    @PatchMapping("/{recruitId}/apply-date")
    public ResponseEntity<RecruitIdResponse> updateApplyDate(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid RecruitApplyDateUpdate recruitApplyDateUpdate,
            @PathVariable long recruitId) {
        Long memberId = loginUser.extractMemberId(token);
        Member requestMember = memberService.getById(memberId);
        Recruit recruit = recruitService.updateApplyDate(requestMember, recruitId, recruitApplyDateUpdate);
        return ResponseEntity
                .ok()
                .body(RecruitIdResponse.from(recruit));
    }
}
