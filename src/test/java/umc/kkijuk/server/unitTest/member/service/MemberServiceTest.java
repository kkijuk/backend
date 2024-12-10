//package umc.kkijuk.server.unitTest.member.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import umc.kkijuk.server.common.domian.exception.*;
//import umc.kkijuk.server.member.controller.response.MemberEmailResponse;
//import umc.kkijuk.server.member.controller.response.MemberInfoResponse;
//import umc.kkijuk.server.member.controller.response.MemberStateResponse;
//import umc.kkijuk.server.member.domain.MarketingAgree;
//import umc.kkijuk.server.member.domain.Member;
//import umc.kkijuk.server.member.domain.State;
//import umc.kkijuk.server.member.dto.*;
//import umc.kkijuk.server.member.repository.MemberRepository;
//import umc.kkijuk.server.member.service.MemberService;
//import umc.kkijuk.server.member.service.MemberServiceImpl;
//import umc.kkijuk.server.unitTest.mock.FakeMemberRepository;
//import umc.kkijuk.server.unitTest.mock.FakePasswordEncoder;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertAll;
//
//
//public class MemberServiceTest {
//    private MemberService memberService;
//    private Member member;
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
//        MemberRepository memberRepository = new FakeMemberRepository();
//        PasswordEncoder passwordEncoder = new FakePasswordEncoder();
//
//        this.memberService = MemberServiceImpl.builder()
//                .memberRepository(memberRepository)
//                .passwordEncoder(passwordEncoder)
//                .build();
//
//        memberRepository.save(member);
//    }
//
//    @Test
//    @DisplayName("[getById] 멤버 ID로 멤버 조회 - 정상 조회")
//    void testGetById() {
//        //given
//        //when
//        Member result = memberService.getById(1L);
//
//        //then
//        assertAll(
//                () -> assertThat(result.getId()).isEqualTo(1L),
//                () -> assertThat(result.getEmail()).isEqualTo(testMemberEmail),
//                () -> assertThat(result.getName()).isEqualTo(testMemberName),
//                () -> assertThat(result.getPhoneNumber()).isEqualTo(testMemberPhoneNumber),
//                () -> assertThat(result.getPassword()).isEqualTo(testMemberPassword),
//                () -> assertThat(result.getBirthDate()).isEqualTo(testMemberBirthDate),
//                () -> assertThat(result.getMarketingAgree()).isEqualTo(testMemberMarketingAgree),
//                () -> assertThat(result.getUserState()).isEqualTo(testMemberState)
//        );
//    }
//
//    @Test
//    @DisplayName("[getById] 존재하지 않는 멤버 조회 시 예외 발생 - ResourceNotFoundException")
//    void testGetByIdDisable() {
//        //given
//        //when
//        //then
//        assertThatThrownBy(
//                () -> memberService.getById(100L))
//                .isInstanceOf(ResourceNotFoundException.class);
//    }
//
//    @Test
//    @DisplayName("[join] 새로운 멤버 가입 - 정상 가입")
//    void testJoin() {
//        //given
//        MemberJoinDto joinMember = MemberJoinDto.builder()
//                .email("new@naver.com")
//                .name("newMember")
//                .phoneNumber("01011112222")
//                .birthDate(LocalDate.of(2002, 1,1))
//                .password("abcd!@")
//                .passwordConfirm("abcd!@")
//                .marketingAgree(MarketingAgree.SMS)
//                .userState(State.ACTIVATE)
//                .build();
//        //when
//        Member newMember = memberService.join(joinMember);
//        //then
//        assertAll(
//                () -> assertThat(newMember.getId()).isEqualTo(2L),
//                () -> assertThat(newMember.getEmail()).isEqualTo("new@naver.com"),
//                () -> assertThat(newMember.getName()).isEqualTo("newMember"),
//                () -> assertThat(newMember.getPhoneNumber()).isEqualTo("01011112222"),
//                () -> assertThat(newMember.getPassword()).isEqualTo("abcd!@"),
//                () -> assertThat(newMember.getBirthDate()).isEqualTo(LocalDate.of(2002, 1,1)),
//                () -> assertThat(newMember.getMarketingAgree()).isEqualTo(MarketingAgree.SMS),
//                () -> assertThat(newMember.getUserState()).isEqualTo(State.ACTIVATE)
//        );
//
//    }
//
//
//
//    @Test
//    @DisplayName("[join] 비밀번호 불일치로 가입 시 예외 발생 - ConfirmPasswordMismatchException")
//    void testJoinDisable() {
//        //given
//        MemberJoinDto joinMember = MemberJoinDto.builder()
//                .email(testMemberEmail)
//                .name("newMember")
//                .phoneNumber("01011112222")
//                .birthDate(LocalDate.of(2002, 1,1))
//                .password("abcd!@")
//                .passwordConfirm("aaaa")
//                .marketingAgree(MarketingAgree.SMS)
//                .userState(State.ACTIVATE)
//                .build();
//        //when
//
//        //then
//        assertThatThrownBy(
//                () -> memberService.join(joinMember))
//                .isInstanceOf(ConfirmPasswordMismatchException.class);
//
//    }
//
//    @Test
//    @DisplayName("[getMemberInfo] 멤버 정보 조회 - 정상 조회")
//    void testGetMemberInfo() {
//        // 여기에 테스트 로직 추가
//        //given
//        //when
//        MemberInfoResponse result = memberService.getMemberInfo(1L);
//        //then
//        assertAll(
//                () -> assertThat(result.getEmail()).isEqualTo(testMemberEmail),
//                () -> assertThat(result.getName()).isEqualTo(testMemberName),
//                () -> assertThat(result.getPhoneNumber()).isEqualTo(testMemberPhoneNumber),
//                () -> assertThat(result.getBirthDate()).isEqualTo(testMemberBirthDate)
//        );
//
//    }
//
//    @Test
//    @DisplayName("[getMemberField] 멤버 필드 정보 조회 - 정상 조회")
//    void testGetMemberField() {
//        //given
//        //when
//        List<String> result = memberService.getMemberField(1L);
//        //then
//        assertThat(result).isEqualTo(testMemberField);
//    }
//
//    @Test
//    @DisplayName("[getMemberEmail] 멤버 이메일 조회 - 정상 조회")
//    void testGetMemberEmail() {
//        //given
//        //when
//        MemberEmailResponse result = memberService.getMemberEmail(1L);
//        //then
//        assertThat(result.getEmail()).isEqualTo(testMemberEmail);
//    }
//
//    @Test
//    @DisplayName("[updateMemberField] 멤버 필드 정보 업데이트 - 정상 업데이트")
//    void testUpdateMemberField() {
//        //given
//        List<String> newField = List.of("test1", "test2", "test3");
//        MemberFieldDto memberFieldDto = MemberFieldDto.builder().field(newField).build();
//        //when
//        Member result = memberService.updateMemberField(1L, memberFieldDto);
//
//        //then
//        assertThat(result.getField()).isEqualTo(newField);
//    }
//
//
////    @Test
////    @DisplayName("[updateMemberField] 기존의 필드 정보로 업데이트 시 예외 발생 - FieldUpdateException")
////    void testUpdateMemberFieldDisable() {
////        //given
////        List<String> newField = List.of("game", "computer");
////        MemberFieldDto memberFieldDto = MemberFieldDto.builder().field(newField).build();
////        //when
////        //then
////        assertThatThrownBy(
////                () -> memberService.updateMemberField(1L, memberFieldDto))
////                .isInstanceOf(FieldUpdateException.class);
////    }
//
//    @Test
//    @DisplayName("[updateMemberInfo] 멤버 정보 업데이트 - 정상 업데이트")
//    void testUpdateMemberInfo() {
//        //given
//        MemberInfoChangeDto memberInfoChangeDto = MemberInfoChangeDto.builder()
//                .phoneNumber("01099998888")
//                .birthDate(LocalDate.of(2022, 01, 01))
//                .marketingAgree(MarketingAgree.NONE)
//                .build();
//        //when
//        Member result = memberService.updateMemberInfo(1L, memberInfoChangeDto);
//        //then
//        assertAll(
//                () -> assertThat(result.getPhoneNumber()).isEqualTo("01099998888"),
//                () -> assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(2022, 01, 01)),
//                () -> assertThat(result.getMarketingAgree()).isEqualTo(MarketingAgree.NONE)
//        );
//    }
//
//
//    @Test
//    @DisplayName("[changeMemberPassword] 비밀번호 변경 - 정상 변경")
//    void testChangeMemberPassword() {
//        //given
//        String currentPW = testMemberPassword;
//        String newPW = "1111";
//        String newPWConfirm = "1111";
//        MemberPasswordChangeDto memberPasswordChangeDto = MemberPasswordChangeDto.builder()
//                .currentPassword(currentPW)
//                .newPassword(newPW)
//                .newPasswordConfirm(newPWConfirm)
//                .build();
//        //when
//        Member result = memberService.changeMemberPassword(1L, memberPasswordChangeDto);
//
//        //then
//        assertThat(result.getPassword()).isEqualTo(newPW);
//    }
//
//    @Test
//    @DisplayName("[changeMemberPassword] 현재 비밀번호 불일치 시 예외 발생 - CurrentPasswordMismatchException")
//    void testChangeMemberPasswordDisable1() {
//        //given
//        String currentPW = "CurrentPasswordMismatchTest";
//        String newPW = "1111";
//        String newPWConfirm = "1111";
//        MemberPasswordChangeDto memberPasswordChangeDto = MemberPasswordChangeDto.builder()
//                .currentPassword(currentPW)
//                .newPassword(newPW)
//                .newPasswordConfirm(newPWConfirm)
//                .build();
//
//        //when
//        //then
//        assertThatThrownBy(
//                () -> memberService.changeMemberPassword(1L, memberPasswordChangeDto))
//                .isInstanceOf(CurrentPasswordMismatchException.class);
//    }
//
//    @Test
//    @DisplayName("[changeMemberPassword] 새 비밀번호와 확인 비밀번호 불일치 시 예외 발생 - ConfirmPasswordMismatchException")
//    void testChangeMemberPasswordDisable2() {
//        //given
//        String currentPW = testMemberPassword;
//        String newPW = "1111";
//        String newPWConfirm = "2222";
//        MemberPasswordChangeDto memberPasswordChangeDto = MemberPasswordChangeDto.builder()
//                .currentPassword(currentPW)
//                .newPassword(newPW)
//                .newPasswordConfirm(newPWConfirm)
//                .build();
//        //when
//        //then
//        assertThatThrownBy(
//                () -> memberService.changeMemberPassword(1L, memberPasswordChangeDto))
//                .isInstanceOf(ConfirmPasswordMismatchException.class);
//    }
//
//    @Test
//    @DisplayName("[myPagePasswordAuth] 마이페이지 비밀번호 인증 - 정상 인증")
//    void testMyPagePasswordAuth() {
//        //given
//        String currentPW = testMemberPassword;
//        MyPagePasswordAuthDto myPagePasswordAuthDto = MyPagePasswordAuthDto.builder()
//                .currentPassword(currentPW)
//                .build();
//        //when
//        Member result = memberService.myPagePasswordAuth(1L, myPagePasswordAuthDto);
//        //then
//        assertThat(currentPW).isEqualTo(result.getPassword());
//    }
//
//    @Test
//    @DisplayName("[myPagePasswordAuth] 비밀번호 인증 실패 시 예외 발생 - CurrentPasswordMismatchException")
//    void testMyPagePasswordAuthDisable() {
//        //given
//        String currentPW = "CurrentPasswordMismatchTest";
//        MyPagePasswordAuthDto myPagePasswordAuthDto = MyPagePasswordAuthDto.builder()
//                .currentPassword(currentPW)
//                .build();
//        //when
//        //then
//        assertThatThrownBy(
//                () -> memberService.myPagePasswordAuth(1L, myPagePasswordAuthDto))
//                .isInstanceOf(CurrentPasswordMismatchException.class);
//    }
//
//    @Test
//    @DisplayName("[changeMemberState] 멤버 상태 업데이트(활성화 <-> 비활성화) - 정상 업데이트")
//    void testChangeMemberState1(){
//        //given
//        //when
//        MemberStateResponse memberStateResponse = memberService.changeMemberState(1L);
//
//        //then
//        assertThat(memberStateResponse.getMemberState()).isEqualTo(State.INACTIVATE);
//
//        MemberStateResponse memberStateResponse2 = memberService.changeMemberState(1L);
//
//        assertThat(memberStateResponse2.getMemberState()).isEqualTo(State.ACTIVATE);
//    }
//
//    @Test
//    @DisplayName("[confirmDupEmail] 이메일 중복 확인, 중복된 이메일 없음 - 정상 인증")
//    public void testConfirmDupEmailTrue() throws Exception {
//        //given
//        String newEmail = "newTestEmail@naver.com";
//        MemberEmailDto memberEmailDto = MemberEmailDto.builder()
//                .email(newEmail)
//                .build();
//
//        //when
//        Boolean result = memberService.confirmDupEmail(memberEmailDto);
//
//        //then
//        assertThat(result).isEqualTo(true);
//    }
//
//    @Test
//    @DisplayName("[confirmDupEmail] 중복 이메일 있을 시 false 리턴")
//    public void testConfirmDupEmailFalse() throws Exception {
//        //given
//        String newEmail = testMemberEmail;
//        MemberEmailDto memberEmailDto = MemberEmailDto.builder()
//                .email(newEmail)
//                .build();
//
//        //when
//        Boolean result = memberService.confirmDupEmail(memberEmailDto);
//
//        //then
//        assertThat(result).isEqualTo(false);
//    }
//
//}
