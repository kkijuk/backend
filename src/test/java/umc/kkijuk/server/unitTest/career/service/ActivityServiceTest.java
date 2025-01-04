package umc.kkijuk.server.unitTest.career.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.kkijuk.server.career.controller.response.ActivityResponse;
import umc.kkijuk.server.career.domain.Activity;
import umc.kkijuk.server.career.dto.ActivityReqDto;
import umc.kkijuk.server.career.repository.ActivityRepository;
import umc.kkijuk.server.career.service.CareerService;
import umc.kkijuk.server.career.service.CareerServiceImpl;
import umc.kkijuk.server.common.domian.exception.OwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.unitTest.mock.FakeActivityRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ActivityServiceTest {
    private CareerService careerService;
    private final Long testMemberId = 3333L;
    private Member requestMember;

    //test Data
    private final LocalDate testStartDate = LocalDate.of(2023, 7, 19);
    private final LocalDate testEndDate = LocalDate.of(2023, 12, 19);
    //fake repository
    private ActivityRepository activityRepository;

    @BeforeEach
    void init() {
        this.requestMember = Member.builder()
                .id(testMemberId)
                .email("test-email@test.com")
                .name("test-name")
                .phoneNumber("test-test-test")
                .birthDate(LocalDate.of(2024, 7, 25))
                .password("test-password")
                .userState(State.ACTIVATE)
                .build();

        this.activityRepository = new FakeActivityRepository();
        this.careerService = CareerServiceImpl.builder()
                .activityRepository(activityRepository)
                .build();

        Activity activity1 = Activity.builder()
                .memberId(testMemberId)
                .name("test activity")
                .alias("test alias")
                .unknown(false)
                .startDate(testStartDate)
                .endDate(testEndDate)
                .organizer("test organizer")
                .role("test role")
                .teamSize(10)
                .contribution(30)
                .isTeam(true)
                .build();

        Activity activity2 = Activity.builder()
                .memberId(testMemberId)
                .name("test activity")
                .alias("test alias")
                .unknown(false)
                .startDate(testStartDate)
                .endDate(testEndDate)
                .organizer("test organizer")
                .role("test role")
                .teamSize(10)
                .contribution(30)
                .isTeam(true)
                .build();

        activityRepository.save(activity1);
        activityRepository.save(activity2);
    }
    @Test
    @DisplayName("[create] 새로운 Activity 만들기 - 정상 요청")
    void testCreateActivity() {
        //given
        ActivityReqDto activityReqDto = ActivityReqDto.builder()
                .name("대외활동")
                .alias("연합동아리")
                .startdate(LocalDate.of(2023,5,1))
                .enddate(LocalDate.of(2023,12,12))
                .isTeam(true)
                .unknown(false)
                .role("백엔드")
                .contribution(30)
                .organizer("컴공선배")
                .teamSize(10)
                .build();
        //when
        ActivityResponse response = careerService.createActivity(requestMember,activityReqDto);
        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(3L),
                () -> assertThat(response.getName()).isEqualTo("대외활동"),
                () -> assertThat(response.getAlias()).isEqualTo("연합동아리"),
                () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2023,12,12)),
                () -> assertThat(response.getIsTeam()).isEqualTo(true),
                () -> assertThat(response.getUnknown()).isEqualTo(false),
                () -> assertThat(response.getRole()).isEqualTo("백엔드"),
                () -> assertThat(response.getContribution()).isEqualTo(30),
                () -> assertThat(response.getOrganizer()).isEqualTo("컴공선배"),
                () -> assertThat(response.getTeamSize()).isEqualTo(10)

        );
    }
    @Test
    @DisplayName("[create] 새로운 Activity 만들기 - null 허용 필드에 null 값 요청, unknown값이 true일 경우 현재 날짜로 설정")
    void testCreateActivity_InvalidFields(){
        //given
        ActivityReqDto activityReqDto = ActivityReqDto.builder()
                .name("대외활동")
                .alias("연합동아리")
                .startdate(LocalDate.of(2023, 5, 1))
                .isTeam(true)
                .unknown(true)
                .contribution(30)
                .organizer("컴공선배")
                .teamSize(10)
                .build();
        //when
        ActivityResponse response = careerService.createActivity(requestMember,activityReqDto);

        //then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getRole()).isNull(),
                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.now())
        );
    }

    @Test
    @DisplayName("[update] 기존에 존재하는 Activity 수정하기 - null 허용 필드에 null 값 요청, unknown값이 true일 경우 현재 날짜로 설정")
    void testUpdateActivity_unknown() {
        //given
        ActivityReqDto activityReqDto = ActivityReqDto.builder()
                .name("수정된 대외활동")
                .alias("수정된 연합동아리")
                .startdate(LocalDate.of(2022, 2, 1))
                .isTeam(false)
                .unknown(true)
                .contribution(100)
                .organizer("수정된 컴공선배")
                .role("update role")
                .teamSize(0)
                .build();
        //when

        //when
        ActivityResponse updatedResponse = careerService.updateActivity(requestMember, 1L, activityReqDto);
        //then
        assertAll(
                () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
                () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 대외활동"),
                () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 연합동아리"),
                () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2022,2,1)),
                () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(updatedResponse.getIsTeam()).isEqualTo(false),
                () -> assertThat(updatedResponse.getUnknown()).isEqualTo(true),
                () -> assertThat(updatedResponse.getRole()).isEqualTo("update role"),
                () -> assertThat(updatedResponse.getContribution()).isEqualTo(100),
                () -> assertThat(updatedResponse.getOrganizer()).isEqualTo("수정된 컴공선배"),
                () -> assertThat(updatedResponse.getTeamSize()).isEqualTo(0)

        );

    }

    @Test
    @DisplayName("[update] 기존에 존재하는 Activity 수정하기 - 정상 요청")
    void testUpdateActivity() {
        //given
        ActivityReqDto activityReqDto = ActivityReqDto.builder()
                .name("수정된 대외활동")
                .alias("수정된 연합동아리")
                .startdate(LocalDate.of(2022, 2, 1))
                .enddate(LocalDate.of(2022,3,1))
                .isTeam(false)
                .unknown(false)
                .contribution(100)
                .organizer("수정된 컴공선배")
                .role("update role")
                .teamSize(0)
                .build();
        //when

        //when
        ActivityResponse updatedResponse = careerService.updateActivity(requestMember, 1L, activityReqDto);
        //then
        assertAll(
                () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
                () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 대외활동"),
                () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 연합동아리"),
                () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2022,2,1)),
                () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.of(2022,3,1)),
                () -> assertThat(updatedResponse.getIsTeam()).isEqualTo(false),
                () -> assertThat(updatedResponse.getUnknown()).isEqualTo(false),
                () -> assertThat(updatedResponse.getRole()).isEqualTo("update role"),
                () -> assertThat(updatedResponse.getContribution()).isEqualTo(100),
                () -> assertThat(updatedResponse.getOrganizer()).isEqualTo("수정된 컴공선배"),
                () -> assertThat(updatedResponse.getTeamSize()).isEqualTo(0)

        );

    }


    @Test
    @DisplayName("[update] 기존에 존재하는 Activity 수정하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testUpdateActivityWithResourceNotFoundException() {
        //given
        ActivityReqDto activityReqDto = ActivityReqDto.builder()
                .name("수정된 대외활동")
                .alias("수정된 연합동아리")
                .startdate(LocalDate.of(2022, 2, 1))
                .isTeam(false)
                .unknown(true)
                .contribution(100)
                .organizer("수정된 컴공선배")
                .teamSize(0)
                .build();

        //when
        //then
        assertThatThrownBy(() -> careerService.updateActivity(requestMember, 999L, activityReqDto)).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    @DisplayName("[update] 기존에 존재하는 Activity 수정하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testUpdateActivityWithOwnerMismatchException() {
        //given
        ActivityReqDto activityReqDto = ActivityReqDto.builder()
                .name("수정된 대외활동")
                .alias("수정된 연합동아리")
                .startdate(LocalDate.of(2022, 2, 1))
                .isTeam(false)
                .unknown(true)
                .contribution(100)
                .organizer("수정된 컴공선배")
                .teamSize(0)
                .build();

        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.updateActivity(anotherMember, 1L, activityReqDto))
                .isInstanceOf(OwnerMismatchException.class);

    }

    @Test
    @DisplayName("[delete] 기존에 존재하는 Activity 제거하기 - 정상 요청")
    void testDeleteActivity() {
        //given
        Long activityId = 1L;
        //when
        careerService.deleteBaseCareer(requestMember,activityId,"activity");
        //then
        assertThat(activityRepository.findById(activityId)).isEmpty();

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Activity 제거하기 - 없는 타입으로의 요청일 경우 IllegalArgumentException 발생")
    void testDeleteActivityWithIllegalArgumentException() {
        //given
        Long activityId = 1L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, activityId, "noneType"))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Activity 제거하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testDeleteActivityWithResourceNotFoundException() {
        //given
        Long activityId = 9999L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, activityId, "activity"))
                .isInstanceOf(ResourceNotFoundException.class);

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Activity 제거하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testDeleteActivityWithOwnerMismatchException() {
        //given
        Long activityId = 1L;
        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(anotherMember, activityId, "activity"))
                .isInstanceOf(OwnerMismatchException.class);
    }
}
