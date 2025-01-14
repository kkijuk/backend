//package umc.kkijuk.server.IntegrationTest.recruit;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.jdbc.SqlGroup;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import umc.kkijuk.server.login.controller.SessionConst;
//import umc.kkijuk.server.login.controller.dto.LoginInfo;
//import umc.kkijuk.server.member.domain.Member;
//import umc.kkijuk.server.recruit.controller.response.RecruitIdResponse;
//import umc.kkijuk.server.recruit.domain.RecruitCreate;
//import umc.kkijuk.server.recruit.domain.RecruitStatus;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@SqlGroup({
//        @Sql(value = "/sql/createTestData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
//        @Sql(value = "/sql/deleteTestData.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//})
//class RecruitCreateTest {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private ObjectMapper objectMapper;
//
//    /**
//     * testValues
//     */
//    private final Long existMemberId =1L;
//    private final LocalDateTime newRecruitStartTime = LocalDateTime.of(2024, 7, 19, 2, 30);
//    private final LocalDateTime newRecruitEndTime = LocalDateTime.of(2024, 7, 30, 2, 30);
//    private final LocalDate newRecruitApplyDate = LocalDate.of(2024, 7, 25);
//
//    @Test
//    @DisplayName("[공고 생성 API] 로그인 정보가 없으면 /api/error로 forward된다.")
//    void testRecruitCreateWithoutLogin() throws Exception {
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
//        String recruitCreateJson = objectMapper.writeValueAsString(recruitCreate);
//
//        //when 로그인 없이 요청을 보낸다.
//        MvcResult result = mockMvc.perform(post("/recruit")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(recruitCreateJson))
//                .andReturn();
//
//        //then
//        assertThat(result.getResponse().getForwardedUrl()).isEqualTo("/api/error");
//    }
//
//    @Test
//    @DisplayName("[공고 생성 API] 로그인 정보가 존재하면 정상 처리된다.")
//    void testRecruitCreateWithLogin() throws Exception {
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
//        String recruitCreateJson = objectMapper.writeValueAsString(recruitCreate);
//
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER_INFO, LoginInfo.from(Member.builder().id(existMemberId).build()));
//
//        //when 로그인 정보와 함께 요청을 보낸다.
//        MvcResult result = mockMvc.perform(post("/recruit")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(recruitCreateJson)
//                        .session(session))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        //then API 응답확인
//        RecruitIdResponse recruitIdResponse = objectMapper.readValue(result.getResponse().getContentAsString(), RecruitIdResponse.class);
//
//        assertThat(recruitIdResponse.getId()).isNotNull();
//    }
//}
