//package umc.kkijuk.server.IntegrationTest.member;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.jdbc.SqlGroup;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import umc.kkijuk.server.login.controller.SessionConst;
//import umc.kkijuk.server.login.controller.dto.LoginInfo;
//import umc.kkijuk.server.member.controller.response.*;
//import umc.kkijuk.server.member.domain.MarketingAgree;
//import umc.kkijuk.server.member.domain.Member;
//import umc.kkijuk.server.member.domain.State;
//import umc.kkijuk.server.member.dto.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@SqlGroup({
//        @Sql(value = "/sql/createTestData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
//        @Sql(value = "/sql/deleteTestData.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//})
//public class MemberTest {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
////    @BeforeEach
////    public void setUp() {
////
////        // 필요한 데이터 삽입
////        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
////        String encodedPassword = passwordEncoder.encode("testPassword");
////        System.out.println("encodedPassword = " + encodedPassword);
////        jdbcTemplate.update("INSERT INTO member (member_id, email, name, phone_number, birth_date, password, marketing_agree, user_state, field, recruit_tags) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
////                1L, "test@test.com", "tester", "010-0000-0000", LocalDate.of(2024, 8, 6), encodedPassword, "BOTH", "ACTIVATE", "[\"game\", \"computer\"]", "[]");
////    }
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    private final Long testMemberId = 1L;
//    private final String testMemberEmail = "test@test.com";
//    private final String testMemberName = "tester";
//    private final String testMemberPhoneNumber = "010-0000-0000";
//    private final LocalDate testMemberBirthDate = LocalDate.of(2024, 8, 6);
//    private final String testMemberPassword = "testPassword";
//    private final MarketingAgree testMemberMarketingAgree = MarketingAgree.BOTH;
//    private final State testMemberState = State.ACTIVATE;
//    private final List<String> testMemberField = List.of("game", "computer");
//
//    @Test
//    @DisplayName("회원가입 성공")
//    public void testSaveMember() throws Exception {
//        //given
//        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
//                .email("test2@naver.com")
//                .name("김철수")
//                .phoneNumber("01099999999")
//                .password("test2password")
//                .passwordConfirm("test2password")
//                .birthDate(LocalDate.of(1999, 3, 31))
//                .marketingAgree(MarketingAgree.BOTH)
//                .userState(State.ACTIVATE)
//                .build();
//
//        String memberJoinDtoJson = objectMapper.writeValueAsString(memberJoinDto);
//
//        //when
//        MvcResult result = mockMvc.perform(post("/member")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(memberJoinDtoJson))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //then
//        CreateMemberResponse createMemberResponse = objectMapper.readValue(result.getResponse().getContentAsString(), CreateMemberResponse.class);
//
//        assertThat(createMemberResponse.getId()).isEqualTo(2L);
//
//        // 회원가입을 통해 받은 세션 확인하는 방법
//        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
//        assertThat(session).isNotNull();
//        LoginInfo sessionAttribute = (LoginInfo) session.getAttribute("LoginMember");
//        assertThat(sessionAttribute).isNotNull();
//    }
//
//
//    @Test
//    @DisplayName("내 정보 조회")
//    public void testGetMemberInfo() throws Exception {
//        //given
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER_INFO, LoginInfo.from(Member.builder().id(testMemberId).build()));
//
//        //when
//        MvcResult result = mockMvc.perform(get("/member/myPage/info")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        //then
//        MemberInfoResponse memberInfoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), MemberInfoResponse.class);
//        assertAll(
//                () -> assertThat(memberInfoResponse.getEmail()).isEqualTo(testMemberEmail),
//                () -> assertThat(memberInfoResponse.getName()).isEqualTo(testMemberName),
//                () -> assertThat(memberInfoResponse.getPhoneNumber()).isEqualTo(testMemberPhoneNumber),
//                () -> assertThat(memberInfoResponse.getBirthDate()).isEqualTo(testMemberBirthDate)
//        );
//    }
//
//    @Test
//    @DisplayName("내 정보 조회 - 로그인 정보가 없을 때 /api/error")
//    public void testGetMemberInfoWithoutLogin() throws Exception {
//        //given
//        //when
//        MvcResult result = mockMvc.perform(get("/member/myPage/info"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        //then
//        assertThat(result.getResponse().getForwardedUrl()).isEqualTo("/api/error");
//
//    }
//
//    @Test
//    @DisplayName("내 정보 수정")
//    public void testChangeMemberInfo() throws Exception {
//        //given
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER_INFO, LoginInfo.from(Member.builder().id(testMemberId).build()));
//
//        MemberInfoChangeDto memberInfoChangeDto = MemberInfoChangeDto.builder()
//                .phoneNumber("01012345678")
//                .birthDate(LocalDate.of(1995, 5, 15))
//                .marketingAgree(MarketingAgree.EMAIL)
//                .build();
//
//        String memberInfoChangeDtoJson = objectMapper.writeValueAsString(memberInfoChangeDto);
//
//        //when
//        mockMvc.perform(put("/member/myPage/info")
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(memberInfoChangeDtoJson))
//                .andExpect(status().isOk());
//
//        //then
//        MvcResult result = mockMvc.perform(get("/member/myPage/info")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        MemberInfoResponse updatedInfo = objectMapper.readValue(result.getResponse().getContentAsString(), MemberInfoResponse.class);
//        assertAll(
//                () -> assertThat(updatedInfo.getPhoneNumber()).isEqualTo("01012345678"),
//                () -> assertThat(updatedInfo.getBirthDate()).isEqualTo(LocalDate.of(1995, 5, 15))
//        );
//    }
//
//    @Test
//    @DisplayName("관심분야 조회")
//    public void testGetMemberField() throws Exception {
//        //given
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER_INFO, LoginInfo.from(Member.builder().id(testMemberId).build()));
//
//        //when
//        MvcResult result = mockMvc.perform(get("/member/myPage/field")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        //then
//        MemberFieldResponse memberFieldResponse = objectMapper.readValue(result.getResponse().getContentAsString(), MemberFieldResponse.class);
//        assertThat(memberFieldResponse.getField()).isEqualTo(testMemberField);
//    }
//
//    @Test
//    @DisplayName("관심분야 등록/수정")
//    public void testPostMemberField() throws Exception {
//        //given
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER_INFO, LoginInfo.from(Member.builder().id(testMemberId).build()));
//
//        MemberFieldDto memberFieldDto = new MemberFieldDto(List.of("sports", "technology"));
//        String memberFieldDtoJson = objectMapper.writeValueAsString(memberFieldDto);
//
//        //when
//        mockMvc.perform(post("/member/myPage/field")
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(memberFieldDtoJson))
//                .andExpect(status().isOk());
//
//        //then
//        MvcResult result = mockMvc.perform(get("/member/myPage/field")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        MemberFieldResponse updatedFieldResponse = objectMapper.readValue(result.getResponse().getContentAsString(), MemberFieldResponse.class);
//        assertThat(updatedFieldResponse.getField()).containsExactly("sports", "technology");
//    }
//
//    @Test
//    @DisplayName("비밀번호 변경")
//    public void testChangeMemberPassword() throws Exception {
//        //given
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER_INFO, LoginInfo.from(Member.builder().id(testMemberId).build()));
//
//        MemberPasswordChangeDto passwordChangeDto = MemberPasswordChangeDto.builder()
//                .currentPassword(testMemberPassword)
//                .newPassword("newPassword123")
//                .newPasswordConfirm("newPassword123")
//                .build();
//
//        String passwordChangeDtoJson = objectMapper.writeValueAsString(passwordChangeDto);
//
//        //when
//        mockMvc.perform(post("/member/myPage/password")
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(passwordChangeDtoJson))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("비밀번호 변경 실패 - 새 비밀번호 확인 불일치")
//    public void testChangeMemberPasswordWithMismatchedNewPasswordConfirm() throws Exception {
//        //given
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER_INFO, LoginInfo.from(Member.builder().id(testMemberId).build()));
//
//        MemberPasswordChangeDto passwordChangeDto = MemberPasswordChangeDto.builder()
//                .currentPassword(testMemberPassword)
//                .newPassword("newPassword123")
//                .newPasswordConfirm("differentPassword")  // 새로운 비밀번호 확인 불일치
//                .build();
//
//        String passwordChangeDtoJson = objectMapper.writeValueAsString(passwordChangeDto);
//
//        //when
//        mockMvc.perform(post("/member/myPage/password")
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(passwordChangeDtoJson))
//                .andExpect(status().isBadRequest());
//    }
//
//
//
//    @Test
//    @DisplayName("내정보 조회 인증 화면 이메일 가져오기")
//    public void testGetEmail() throws Exception {
//        //given
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER_INFO, LoginInfo.from(Member.builder().id(testMemberId).build()));
//
//        //when
//        MvcResult result = mockMvc.perform(get("/member/myPage")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        //then
//        MemberEmailResponse emailResponse = objectMapper.readValue(result.getResponse().getContentAsString(), MemberEmailResponse.class);
//        assertThat(emailResponse.getEmail()).isEqualTo(testMemberEmail);
//    }
//
//    @Test
//    @DisplayName("내정보 조회용 비밀번호 인증")
//    public void testMyPagePasswordAuth() throws Exception {
//        //given
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER_INFO, LoginInfo.from(Member.builder().id(testMemberId).build()));
//
//        MyPagePasswordAuthDto authDto = MyPagePasswordAuthDto.builder()
//                .currentPassword(testMemberPassword)
//                .build();
//
//        String authDtoJson = objectMapper.writeValueAsString(authDto);
//
//        //when
//        mockMvc.perform(post("/member/myPage")
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(authDtoJson))
//                .andExpect(status().isOk());
//    }
//
//
//    @Test
//    @DisplayName("회원 탈퇴 (상태 비활성화)")
//    public void testMemberInactivate() throws Exception {
//        //given
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER_INFO, LoginInfo.from(Member.builder().id(testMemberId).build()));
//
//        //when
//        MvcResult result = mockMvc.perform(patch("/member/inactive")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        //then
//        MemberStateResponse stateResponse = objectMapper.readValue(result.getResponse().getContentAsString(), MemberStateResponse.class);
//        assertThat(stateResponse.getMemberState()).isEqualTo(State.INACTIVATE);
//    }
//}
