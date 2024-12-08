//package umc.kkijuk.server.unitTest.recruit.controller;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import umc.kkijuk.server.login.controller.dto.LoginInfo;
//import umc.kkijuk.server.member.domain.Member;
//import umc.kkijuk.server.recruit.controller.RecruitController;
//import umc.kkijuk.server.recruit.controller.response.RecruitIdResponse;
//import umc.kkijuk.server.recruit.domain.RecruitCreate;
//import umc.kkijuk.server.recruit.domain.RecruitStatus;
//import umc.kkijuk.server.unitTest.mock.TestContainer;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//public class RecruitControllerTest {
//    public RecruitController recruitController;
//    public final Long requestMemberId = 1L;
//    public LoginInfo loginInfo;
//
//    /**
//     * TestValues
//     */
//    private final LocalDateTime newRecruitStartTime = LocalDateTime.of(2024, 7, 19, 2, 30);
//    private final LocalDateTime newRecruitEndTime = LocalDateTime.of(2024, 7, 30, 2, 30);
//    private final LocalDate newRecruitApplyDate = LocalDate.of(2024, 7, 25);
//
//    @BeforeEach
//    void init() {
//        TestContainer testContainer = TestContainer.builder().build();
//        recruitController = RecruitController.builder()
//                .memberService(testContainer.memberService)
//                .recruitService(testContainer.recruitService)
//                .memberService(testContainer.memberService)
//                .build();
//
//        Member requestMember = Member.builder().id(requestMemberId).build();
//        testContainer.memberRepository.save(requestMember);
//        loginInfo = LoginInfo.from(requestMember);
//    }
//
//
//    @Test
//    @DisplayName("[create] 새로운 지원 공고 생성")
//    void testCreate() {
//        //given
//        RecruitCreate recruitCreate = RecruitCreate.builder()
//                .title("dto-title")
//                .status(RecruitStatus.PLANNED)
//                .startTime(newRecruitStartTime)
//                .endTime(newRecruitEndTime)
//                .applyDate(newRecruitApplyDate)
//                .tags(new ArrayList<>(Arrays.asList("tag1", "tag2", "tag3")))
//                .link("https://www.dto-title.com")
//                .build();
//
//        //when
//        ResponseEntity<RecruitIdResponse> result = recruitController.create(loginInfo, recruitCreate);
//        RecruitIdResponse body = result.getBody();
//
//        //then
//        Assertions.assertAll(
//                () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED),
//                () -> assertThat(body).isNotNull(),
//                () -> assertThat(body.getId()).isNotNull()
//        );
//    }
//
//    @Test
//    @DisplayName("[create] 없는 사용자의 요청")
//    void testCreateResourceNotFoundException() {
//        //given
//        final Long NotExistingMemberId = 9999L;
//
//        RecruitCreate recruitCreate = RecruitCreate.builder()
//                .title("dto-title")
//                .status(RecruitStatus.PLANNED)
//                .startTime(newRecruitStartTime)
//                .endTime(newRecruitEndTime)
//                .applyDate(newRecruitApplyDate)
//                .tags(new ArrayList<>(Arrays.asList("tag1", "tag2", "tag3")))
//                .link("https://www.dto-title.com")
//                .build();
//
//        LoginInfo NotExistLoginInfo = LoginInfo.builder().memberId(NotExistingMemberId).build();
//
//        //when
//        //then
//        assertThatThrownBy(() -> recruitController.create(NotExistLoginInfo, recruitCreate));
//    }
//}
