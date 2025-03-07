//package umc.kkijuk.server.unitTest.career.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import umc.kkijuk.server.career.controller.response.ActivityResponse;
//import umc.kkijuk.server.career.domain.Activity;
//import umc.kkijuk.server.career.dto.ActivityReqDto;
//import umc.kkijuk.server.career.repository.ActivityRepository;
//import umc.kkijuk.server.career.service.CareerService;
//import umc.kkijuk.server.career.service.CareerServiceImpl;
//import umc.kkijuk.server.member.domain.Member;
//import umc.kkijuk.server.member.domain.State;
//import umc.kkijuk.server.record.repository.RecordRepository;
//import umc.kkijuk.server.unitTest.mock.FakeActivityRepository;
//
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//
//public class CareerServiceTest {
//    private CareerService careerService;
//    private final Long testMemberId = 3333L;
//    private Member requestMember;
//
//    //test Data
//    private final LocalDate testStartDate = LocalDate.of(2023, 7, 19);
//    private final LocalDate testEndDate = LocalDate.of(2023, 12, 19);
//
//    @BeforeEach
//    void init() {
//        this.requestMember = Member.builder()
//                .id(testMemberId)
//                .email("test-email@test.com")
//                .name("test-name")
//                .phoneNumber("test-test-test")
//                .birthDate(LocalDate.of(2024, 7, 25))
//                .userState(State.ACTIVATE)
//                .build();
//
//        ActivityRepository activityRepository = new FakeActivityRepository();
//
//
//        this.careerService = CareerServiceImpl.builder()
//                .activityRepository(activityRepository)
//                .build();
//
//        Activity activity1 = Activity.builder()
//                .memberId(testMemberId)
//                .name("test activity")
//                .alias("test alias")
//                .unknown(false)
//                .startDate(testStartDate)
//                .endDate(testEndDate)
//                .organizer("test organizer")
//                .role("test role")
//                .teamSize(10)
//                .contribution(30)
//                .isTeam(true)
//                .build();
//
//        Activity activity2 = Activity.builder()
//                .memberId(testMemberId)
//                .name("test activity")
//                .alias("test alias")
//                .unknown(false)
//                .startDate(testStartDate)
//                .endDate(testEndDate)
//                .organizer("test organizer")
//                .role("test role")
//                .teamSize(10)
//                .contribution(30)
//                .isTeam(true)
//                .build();
//
//        activityRepository.save(activity1);
//        activityRepository.save(activity2);
//    }
//    @Test
//    @DisplayName("[create] 새로운 Activity 만들기 - 정상 요청")
//    void testCreateActivity() {
//        //given
//        ActivityReqDto activityReqDto = ActivityReqDto.builder()
//                .name("대외활동")
//                .alias("연합동아리")
//                .startdate(LocalDate.of(2023,5,1))
//                .enddate(LocalDate.of(2023,12,12))
//                .isTeam(true)
//                .unknown(false)
//                .role("백엔드")
//                .contribution(30)
//                .organizer("컴공선배")
//                .teamSize(10)
//                .build();
//        //when
//        ActivityResponse response = careerService.createActivity(requestMember,activityReqDto);
//        //then
//        assertAll(
//                () -> assertThat(response.getId()).isEqualTo(3L),
//                () -> assertThat(response.getName()).isEqualTo("대외활동"),
//                () -> assertThat(response.getAlias()).isEqualTo("연합동아리"),
//                () -> assertThat(response.getStartDate()).isEqualTo(LocalDate.of(2023,5,1)),
//                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2023,12,12)),
//                () -> assertThat(response.getIsTeam()).isEqualTo(true),
//                () -> assertThat(response.getUnknown()).isEqualTo(false),
//                () -> assertThat(response.getRole()).isEqualTo("백엔드"),
//                () -> assertThat(response.getContribution()).isEqualTo(30),
//                () -> assertThat(response.getOrganizer()).isEqualTo("컴공선배"),
//                () -> assertThat(response.getTeamSize()).isEqualTo(10)
//
//        );
//    }
//    @Test
//    @DisplayName("[create] 새로운 Activity 만들기 - null 허용 필드에 null 값 요청, endDate 값 null일 경우 현재 날짜로 설정")
//    void testCreateActivity_InvalidFields(){
//        //given
//        ActivityReqDto activityReqDto = ActivityReqDto.builder()
//                .name("대외활동")
//                .alias("연합동아리")
//                .startdate(LocalDate.of(2023, 5, 1))
//                .isTeam(true)
//                .unknown(true)
//                .contribution(30)
//                .organizer("컴공선배")
//                .teamSize(10)
//                .build();
//        //when
//        ActivityResponse response = careerService.createActivity(requestMember,activityReqDto);
//
//        //then
//        assertAll(
//                () -> assertThat(response).isNotNull(),
//                () -> assertThat(response.getRole()).isNull(),
//                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.now())
//        );
//    }
//}
