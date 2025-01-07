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
import umc.kkijuk.server.login.argumentresolver.Login;
import umc.kkijuk.server.login.controller.dto.LoginInfo;
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

    @Operation(
            summary = "지원 공고 생성",
            description = "주어진 정보를 바탕으로 지원 공고 데이터를 생성합니다.")
    @PostMapping
    public ResponseEntity<RecruitIdResponse> create(
            @Login LoginInfo loginInfo,
            @RequestBody @Valid RecruitCreate recruitCreate
    ) {
        Member requestMember = memberService.getById(loginInfo.getMemberId());
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
    public ResponseEntity<RecruitIdResponse> update(
            @Login LoginInfo loginInfo,
            @RequestBody @Valid RecruitUpdate recruitUpdate,
            @PathVariable long recruitId) {
        Member requestMember = memberService.getById(loginInfo.getMemberId());
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
    public ResponseEntity<RecruitIdResponse> updateState(
            @Login LoginInfo loginInfo,
            @RequestBody @Valid RecruitStatusUpdate recruitStatusUpdate,
            @PathVariable long recruitId) {
        Member requestMember = memberService.getById(loginInfo.getMemberId());
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
    public ResponseEntity<RecruitIdResponse> delete(
            @Login LoginInfo loginInfo,
            @PathVariable long recruitId) {
        Member requestMember = memberService.getById(loginInfo.getMemberId());
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
    public ResponseEntity<RecruitInfoResponse> getRecruitInfo(
            @Login LoginInfo loginInfo,
            @PathVariable long recruitId) {
        Member requestMember = memberService.getById(loginInfo.getMemberId());
        Recruit recruit = recruitService.getById(recruitId);
        List<Review> reviews = reviewService.findAllByRecruit(requestMember, recruit);

        return ResponseEntity
                .ok()
                .body(RecruitInfoResponse.from(recruit, reviews));
    }

    //수정 필요 -> 태그 필요
    @Operation(
        summary = "지원 공고 목록 (특정 날짜 이후)",
        description = "주어진 날짜에 마감 종료되는 지원 공고들의 목록을 요청합니다.")
    @GetMapping("/list/end")
    public ResponseEntity<RecruitListByEndDateResponse> findAllRecruitListByEndTime(
            @Login LoginInfo loginInfo,
            @Parameter(name = "date", description = "지원 공고 마감 날짜", example = "2024-07-20")
            @RequestParam LocalDate date) {
        Member requestMember = memberService.getById(loginInfo.getMemberId());
        Map<Recruit, String> recruits = recruitService.findAllByEndTime(requestMember, date);
        return ResponseEntity
                .ok()
                .body(RecruitListByEndDateResponse.from(recruits));
    }

    //수정 필요 -> 태그 필요
    @Operation(
            summary = "지원 공고 목록 (특정 시간 이후)",
            description = "주어진 시간 이후 마감 종료되는 지원 공고들의 목록을 요청합니다.")
    @GetMapping("/list/after")
    public ResponseEntity<RecruitListByEndTimeAfterResponse> findAllRecruitListByEndTimeAfterNow(
            @Login LoginInfo loginInfo,
            @Parameter(name = "time", description = "이 시간 이후에 마감되는 공고를 요청", example = "2024-07-20 10:30")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
            @RequestParam LocalDateTime time) {
        Member requestMember = memberService.getById(loginInfo.getMemberId());
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
            @Login LoginInfo loginInfo,
            @Parameter(name = "time", description = "이 시간 이후에 유효한 공고 목록 요청", example = "2024-07-20 10:30")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
            @RequestParam LocalDateTime time
    ) {
        Member requestMember = memberService.getById(loginInfo.getMemberId());
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
            @Login LoginInfo loginInfo,
            @Parameter(name = "year", description = "년도", example = "2024")
            @RequestParam Integer year,
            @Parameter(name = "month", description = "월", example = "7")
            @RequestParam Integer month) {
        Member requestMember = memberService.getById(loginInfo.getMemberId());
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
            @Login LoginInfo loginInfo,
            @RequestBody @Valid RecruitApplyDateUpdate recruitApplyDateUpdate,
            @PathVariable long recruitId) {
        Member requestMember = memberService.getById(loginInfo.getMemberId());
        Recruit recruit = recruitService.updateApplyDate(requestMember, recruitId, recruitApplyDateUpdate);
        return ResponseEntity
                .ok()
                .body(RecruitIdResponse.from(recruit));
    }
}