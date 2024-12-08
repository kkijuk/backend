//package umc.kkijuk.server.unitTest.career.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import umc.kkijuk.server.career.controller.BaseCareerController;
//import umc.kkijuk.server.career.controller.response.*;
//import umc.kkijuk.server.career.domain.*;
//import umc.kkijuk.server.career.dto.*;
//import umc.kkijuk.server.career.service.BaseCareerServiceImpl;
//import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
//import umc.kkijuk.server.detail.domain.CareerType;
//import umc.kkijuk.server.member.domain.Member;
//import umc.kkijuk.server.member.service.MemberServiceImpl;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class CareerControllerTest {
//    @InjectMocks
//    private BaseCareerController careerController;
//    @Mock
//    private BaseCareerServiceImpl baseCareerService;
//    @Mock
//    private MemberServiceImpl memberService;
//    public final Long requestMemberId = 1L;
//    public LoginInfo loginInfo;
//    public Member requestMember;
//    public Activity testActivity;
//    public Circle testCircle;
//    public Competition testCompetition;
//    @BeforeEach
//    void init() {
//        requestMember = Member.builder()
//                .id(requestMemberId)
//                .build();
//        loginInfo = LoginInfo.from(requestMember);
//
//        testActivity = Activity.builder()
//                        .memberId(requestMemberId)
//                        .name("testname")
//                        .alias("testalias")
//                        .unknown(true)
//                        .isTeam(true)
//                        .startdate(LocalDate.of(2023,1,1))
//                        .enddate(LocalDate.of(2024,1,1))
//                        .contribution(30)
//                        .role("testrole")
//                        .teamSize(8)
//                        .organizer("testorganizer")
//                        .build();
//
//        testCircle = Circle.builder()
//                .memberId(requestMemberId)
//                .name("testname")
//                .alias("testalias")
//                .unknown(true)
//                .startdate(LocalDate.of(2023,1,1))
//                .enddate(LocalDate.of(2024,1,1))
//                .role("testrole")
//                .location(true)
//                .build();
//
//        testCompetition = Competition.builder()
//                .memberId(requestMemberId)
//                .name("testname")
//                .alias("testalias")
//                .unknown(true)
//                .isTeam(true)
//                .startdate(LocalDate.of(2023,1,1))
//                .enddate(LocalDate.of(2024,1,1))
//                .contribution(30)
//                .isTeam(true)
//                .teamSize(8)
//                .organizer("testorganizer")
//                .build();
//
//    }
//    @Test
//    @DisplayName("[create activity] 새로운 Activity 생성")
//    void testActivity() {
//        //given
//        ActivityReqDto request = ActivityReqDto.builder()
//                .name("testname")
//                .alias("testalias")
//                .unknown(false)
//                .isTeam(true)
//                .startdate(LocalDate.of(2023,1,1))
//                .enddate(LocalDate.of(2024,1,1))
//                .contribution(30)
//                .role("testrole")
//                .teamSize(8)
//                .organizer("testorganizer")
//                .build();
//
//        //when
//        when(memberService.getById(requestMemberId)).thenReturn(requestMember);
//        when(baseCareerService.createActivity(requestMember,request)).thenReturn(new ActivityResponse(testActivity));
//
//        CareerResponse<ActivityResponse> response = careerController.createActivity(loginInfo,request);
//        ActivityResponse result = response.getData();
//
//        //then
//        assertAll(
//                () -> assertEquals(response.getMessage(), CareerResponseMessage.CAREER_CREATE_SUCCESS),
//                () -> assertEquals(result.getCategory(), CareerType.ACTIVITY.getDescription()),
//                () -> assertEquals(result.getName(),request.getName()),
//                () -> assertEquals(result.getAlias(),request.getAlias()),
//                () -> assertEquals(result.getStartdate(),request.getStartdate()),
//                () -> assertEquals(result.getEndDate(),request.getEnddate()),
//                () -> assertEquals(result.getOrganizer(), request.getOrganizer())
//        );
//
//    }
//    @Test
//    @DisplayName("[create activity] 없는 사용자의 요청")
//    void testCreateActivityResourceNotFoundException() {
//        //given
//        final Long NotExistingMemberId = 9999L;
//        LoginInfo NotExistLoginInfo = LoginInfo.builder().memberId(NotExistingMemberId).build();
//
//        ActivityReqDto request = ActivityReqDto.builder()
//                .name("testname")
//                .alias("testalias")
//                .unknown(false)
//                .isTeam(true)
//                .startdate(LocalDate.of(2023,1,1))
//                .enddate(LocalDate.of(2024,1,1))
//                .contribution(30)
//                .role("testrole")
//                .teamSize(8)
//                .organizer("testorganizer")
//                .build();
//
//        //when
//        when(memberService.getById(NotExistingMemberId))
//                .thenThrow(new ResourceNotFoundException("Member", NotExistingMemberId));
//
//        //then
//        assertThrows(ResourceNotFoundException.class, () ->{
//            careerController.createActivity(NotExistLoginInfo,request);
//        });
//    }
//    @Test
//    @DisplayName("[create circle] 새로운 Circle 생성")
//    void testCircle() {
//        //given
//        CircleReqDto request = CircleReqDto.builder()
//                .name("testname")
//                .alias("testalias")
//                .unknown(false)
//                .startdate(LocalDate.of(2023,1,1))
//                .enddate(LocalDate.of(2024,1,1))
//                .location(true)
//                .role("testrole")
//                .build();
//
//        //when
//        when(memberService.getById(requestMemberId)).thenReturn(requestMember);
//        when(baseCareerService.createCircle(requestMember,request)).thenReturn(new CircleResponse(testCircle));
//        CareerResponse<CircleResponse> response = careerController.createCircle(loginInfo,request);
//        CircleResponse result = response.getData();
//
//        //then
//        assertAll(
//                () -> assertEquals(response.getMessage(), CareerResponseMessage.CAREER_CREATE_SUCCESS),
//                () -> assertEquals(result.getCategory(), CareerType.CIRCLE.getDescription()),
//                () -> assertEquals(result.getName(),request.getName()),
//                () -> assertEquals(result.getAlias(),request.getAlias()),
//                () -> assertEquals(result.getStartdate(),request.getStartdate()),
//                () -> assertEquals(result.getEndDate(),request.getEnddate()),
//                () -> assertEquals(result.getLocation(), request.getLocation()),
//                () -> assertEquals(result.getRole(), request.getRole())
//        );
//
//
//
//    }
//    @Test
//    @DisplayName("[create circle] 없는 사용자의 요청")
//    void testCreateCircleResourceNotFoundException() {
//        //given
//        final Long NotExistingMemberId = 9999L;
//        LoginInfo NotExistLoginInfo = LoginInfo.builder().memberId(NotExistingMemberId).build();
//
//        CircleReqDto request = CircleReqDto.builder()
//                .name("testname")
//                .alias("testalias")
//                .unknown(false)
//                .startdate(LocalDate.of(2023,1,1))
//                .enddate(LocalDate.of(2024,1,1))
//                .location(true)
//                .role("testrole")
//                .build();
//
//        //when
//        when(memberService.getById(NotExistingMemberId))
//                .thenThrow(new ResourceNotFoundException("Member", NotExistingMemberId));
//
//        //then
//        assertThrows(ResourceNotFoundException.class, () ->{
//            careerController.createCircle(NotExistLoginInfo,request);
//        });
//
//    }
//    @Test
//    @DisplayName("[create competition] 새로운 Competition 생성")
//    void testCompetition() {
//        //given
//        CompetitionReqDto request = CompetitionReqDto.builder()
//                .name("testname")
//                .alias("testalias")
//                .unknown(false)
//                .isTeam(true)
//                .startdate(LocalDate.of(2023,1,1))
//                .enddate(LocalDate.of(2024,1,1))
//                .isTeam(true)
//                .contribution(30)
//                .teamSize(8)
//                .organizer("testorganizer")
//                .build();
//        //when
//        when(memberService.getById(requestMemberId)).thenReturn(requestMember);
//        when(baseCareerService.createCompetition(requestMember,request)).thenReturn(new CompetitionResponse(testCompetition));
//        CareerResponse<CompetitionResponse> response = careerController.createComp(loginInfo,request);
//        CompetitionResponse result = response.getData();
//        //then
//        assertAll(
//                () -> assertEquals(response.getMessage(), CareerResponseMessage.CAREER_CREATE_SUCCESS),
//                () -> assertEquals(result.getCategory(), CareerType.COM.getDescription()),
//                () -> assertEquals(result.getName(),request.getName()),
//                () -> assertEquals(result.getAlias(),request.getAlias()),
//                () -> assertEquals(result.getStartdate(),request.getStartdate()),
//                () -> assertEquals(result.getEndDate(),request.getEnddate()),
//                () -> assertEquals(result.getContribution(), request.getContribution()),
//                () -> assertEquals(result.getTeamSize(), request.getTeamSize()),
//                () -> assertEquals(result.getOrganizer(),request.getOrganizer()),
//                () -> assertEquals(result.getIsTeam(),request.getIsTeam())
//        );
//
//    }
//    @Test
//    @DisplayName("[create competition] 없는 사용자의 요청")
//    void testCreateCompetitionResourceNotFoundException() {
//        //given
//        final Long NotExistingMemberId = 9999L;
//        LoginInfo NotExistLoginInfo = LoginInfo.builder().memberId(NotExistingMemberId).build();
//
//        CompetitionReqDto request = CompetitionReqDto.builder()
//                .name("testname")
//                .alias("testalias")
//                .unknown(false)
//                .isTeam(true)
//                .startdate(LocalDate.of(2023,1,1))
//                .enddate(LocalDate.of(2024,1,1))
//                .isTeam(true)
//                .contribution(30)
//                .teamSize(8)
//                .organizer("testorganizer")
//                .build();
//
//        //when
//        when(memberService.getById(NotExistingMemberId))
//                .thenThrow(new ResourceNotFoundException("Member", NotExistingMemberId));
//
//        //then
//        assertThrows(ResourceNotFoundException.class, () ->{
//            careerController.createComp(NotExistLoginInfo,request);
//        });
//
//    }
//
//}
