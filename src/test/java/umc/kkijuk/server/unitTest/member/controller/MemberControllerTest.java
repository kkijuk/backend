//package umc.kkijuk.server.unitTest.member.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import umc.kkijuk.server.login.controller.SessionConst;
//import umc.kkijuk.server.login.controller.dto.LoginInfo;
//import umc.kkijuk.server.member.controller.MemberController;
//import umc.kkijuk.server.member.controller.response.*;
//import umc.kkijuk.server.member.domain.MarketingAgree;
//import umc.kkijuk.server.member.domain.Member;
//import umc.kkijuk.server.member.domain.State;
//import umc.kkijuk.server.member.dto.*;
//import umc.kkijuk.server.unitTest.mock.TestContainer;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.mockito.Mockito.mock;
//
//public class MemberControllerTest {
//    private TestContainer testContainer;
//    private MemberController memberController;
//    private Member member;
//    private LoginInfo loginInfo;
//
//    /**
//     * testValues
//     */
//    private final String testMemberEmail = "test@naver.com";
//    private final String testMemberName = "홍길동";
//    private final String testMemberPhoneNumber = "01012345678";
//    private final LocalDate testMemberBirthDate = LocalDate.of(1999, 3, 31);
//    private final String testMemberPassword = "test-password";
//    private final MarketingAgree testMemberMarketingAgree = MarketingAgree.BOTH;
//    private final State testMemberState = State.ACTIVATE;
//    private final List<String> testMemberField = List.of("game", "computer");
//
//    @BeforeEach
//    void init() {
//        testContainer = TestContainer.builder().build();
//        member = Member.builder()
//                .email(testMemberEmail)
//                .name(testMemberName)
//                .phoneNumber(testMemberPhoneNumber)
//                .birthDate(testMemberBirthDate)
//                .password(testMemberPassword)
//                .marketingAgree(testMemberMarketingAgree)
//                .userState(testMemberState)
//                .field(testMemberField)
//                .build();
//
//        memberController = MemberController.builder()
//                .memberService(testContainer.memberService)
//                .loginService(testContainer.loginService)
//                .build();
//
//        Member saveMember = testContainer.memberRepository.save(member);
//        loginInfo = LoginInfo.from(saveMember);
//    }
//
//    @Test
//    @DisplayName("회원가입 요청")
//    void testSaveMember() {
//        //given
//        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
//                .email("new@naver.com")
//                .name("김철수")
//                .phoneNumber("01098765432")
//                .password("new-password")
//                .passwordConfirm("new-password")
//                .birthDate(LocalDate.of(2000, 1, 1))
//                .marketingAgree(MarketingAgree.EMAIL)
//                .userState(State.ACTIVATE)
//                .build();
//
//        //when
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        ResponseEntity<CreateMemberResponse> result = memberController.saveMember(memberJoinDto,request, response);
//
//        //then
//        assertAll(
//                () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED),
//                () -> {
//                    CreateMemberResponse body = result.getBody();
//                    assertThat(body).isNotNull();
//                    assertThat(body.getId()).isNotNull();
//                    assertThat(body.getMessage()).isEqualTo("Member created successfully");
//                }
//        );
//    }
//
//    @Test
//    @DisplayName("내 정보 조회")
//    void testGetInfo() {
//        //when
//        ResponseEntity<MemberInfoResponse> response = memberController.getInfo(loginInfo);
//
//        //then
//        assertAll(
//                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
//                () -> {
//                    MemberInfoResponse body = response.getBody();
//                    assertThat(body).isNotNull();
//                    assertThat(body.getEmail()).isEqualTo(testMemberEmail);
//                    assertThat(body.getName()).isEqualTo(testMemberName);
//                    assertThat(body.getPhoneNumber()).isEqualTo(testMemberPhoneNumber);
//                    assertThat(body.getBirthDate()).isEqualTo(testMemberBirthDate);
//                }
//        );
//    }
//
//    @Test
//    @DisplayName("내 정보 수정")
//    void testChangeMemberInfo() {
//        //given
//        MemberInfoChangeDto memberInfoChangeDto = MemberInfoChangeDto.builder()
//                .phoneNumber("01087654321")
//                .birthDate(LocalDate.of(1995, 5, 15))
//                .marketingAgree(MarketingAgree.NONE)
//                .build();
//
//        //when
//        ResponseEntity<Boolean> response = memberController.changeMemberInfo(loginInfo, memberInfoChangeDto);
//
//        //then
//        assertAll(
//                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
//                () -> assertThat(response.getBody()).isTrue(),
//                () -> {
//                    MemberInfoResponse updatedMember = memberController.getInfo(loginInfo).getBody();
//                    assertThat(updatedMember).isNotNull();
//                    assertThat(updatedMember.getPhoneNumber()).isEqualTo("01087654321");
//                    assertThat(updatedMember.getBirthDate()).isEqualTo(LocalDate.of(1995, 5, 15));
//                }
//        );
//    }
//
//    @Test
//    @DisplayName("관심분야 조회")
//    void testGetField() {
//        //when
//        ResponseEntity<MemberFieldResponse> response = memberController.getField(loginInfo);
//
//        //then
//        assertAll(
//                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
//                () -> {
//                    MemberFieldResponse body = response.getBody();
//                    assertThat(body).isNotNull();
//                    assertThat(body.getField()).isEqualTo(testMemberField);
//                }
//        );
//    }
//
//    @Test
//    @DisplayName("관심분야 등록/수정")
//    void testPostField() {
//        //given
//        MemberFieldDto memberFieldDto = MemberFieldDto.builder()
//                .field(List.of("sports", "music"))
//                .build();
//
//        //when
//        ResponseEntity<Boolean> response = memberController.postField(loginInfo, memberFieldDto);
//
//        //then
//        assertAll(
//                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
//                () -> assertThat(response.getBody()).isTrue(),
//                () -> {
//                    ResponseEntity<MemberFieldResponse> fieldResponse = memberController.getField(loginInfo);
//                    assertThat(fieldResponse.getBody().getField()).containsExactly("sports", "music");
//                }
//        );
//    }
//
//    @Test
//    @DisplayName("비밀번호 변경")
//    void testChangeMemberPassword() {
//        //given
//        MemberPasswordChangeDto passwordChangeDto = MemberPasswordChangeDto.builder()
//                .currentPassword(testMemberPassword)
//                .newPassword("new-password")
//                .newPasswordConfirm("new-password")
//                .build();
//
//        //when
//        ResponseEntity<Boolean> response = memberController.changeMemberPassword(loginInfo, passwordChangeDto);
//
//        //then
//        assertAll(
//                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
//                () -> assertThat(response.getBody()).isTrue(),
//                () -> {
//                    Optional<Member> updatedMember = testContainer.memberRepository.findById(loginInfo.getMemberId());
//                    assertThat(updatedMember.isPresent()).isTrue();
//                    assertThat(updatedMember.get().getPassword()).isEqualTo("new-password");
//                }
//        );
//    }
//
//    @Test
//    @DisplayName("내정보 조회 인증 화면 이메일 가져오기")
//    void testGetEmail() {
//        //when
//        ResponseEntity<MemberEmailResponse> response = memberController.getEmail(loginInfo);
//
//        //then
//        assertAll(
//                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
//                () -> {
//                    MemberEmailResponse body = response.getBody();
//                    assertThat(body).isNotNull();
//                    assertThat(body.getEmail()).isEqualTo(testMemberEmail);
//                }
//        );
//    }
//
//    @Test
//    @DisplayName("내정보 조회용 비밀번호 인증")
//    void testMyPagePasswordAuth() {
//        //given
//        MyPagePasswordAuthDto myPagePasswordAuthDto = MyPagePasswordAuthDto.builder()
//                .currentPassword(testMemberPassword)
//                .build();
//
//        //when
//        ResponseEntity<Boolean> response = memberController.myPagePasswordAuth(loginInfo, myPagePasswordAuthDto);
//
//        //then
//        assertAll(
//                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
//                () -> assertThat(response.getBody()).isTrue()
//        );
//    }
//
//    @Test
//    @DisplayName("회원 탈퇴 (상태 비활성화)")
//    void testMemberInactivate() {
//        //when
//        ResponseEntity<MemberStateResponse> response = memberController.memberInactivate(loginInfo);
//
//        //then
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        MemberStateResponse body = response.getBody();
//        assertThat(body).isNotNull();
//        assertThat(body.getMemberState()).isEqualTo(State.INACTIVATE);
//
//        // Reactivate the member and check again
//        ResponseEntity<MemberStateResponse> reactivatedResponse = memberController.memberInactivate(loginInfo);
//        assertThat(reactivatedResponse.getBody().getMemberState()).isEqualTo(State.ACTIVATE);
//    }
//
//
//
//
//}
