package umc.kkijuk.server.unitTest.career.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.kkijuk.server.career.controller.response.CircleResponse;
import umc.kkijuk.server.career.domain.Circle;
import umc.kkijuk.server.career.dto.CircleReqDto;
import umc.kkijuk.server.career.repository.CircleRepository;
import umc.kkijuk.server.career.service.CareerService;
import umc.kkijuk.server.career.service.CareerServiceImpl;
import umc.kkijuk.server.common.domian.exception.OwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.unitTest.mock.FakeCircleRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CircleServiceTest {
    private CareerService careerService;
    private final Long testMemberId = 3333L;
    private Member requestMember;

    //test Data
    private final LocalDate testStartDate = LocalDate.of(2023, 7, 19);
    private final LocalDate testEndDate = LocalDate.of(2023, 12, 19);
    //fake repository
    private CircleRepository circleRepository;

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

        this.circleRepository = new FakeCircleRepository();
        this.careerService = CareerServiceImpl.builder()
                .circleRepository(circleRepository)
                .build();

        Circle circle1 = Circle.builder()
                .memberId(testMemberId)
                .name("test activity")
                .alias("test alias")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .location(true)
                .role("test role")
                .build();

        Circle circle2 = Circle.builder()
                .memberId(testMemberId)
                .name("test activity")
                .alias("test alias")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .location(true)
                .role("test role")
                .build();

        circleRepository.save(circle1);
        circleRepository.save(circle2);
    }
    @Test
    @DisplayName("[create] 새로운 Circle 만들기 - 정상 요청")
    void testCreateCircle() {
        //given
        CircleReqDto circleReqDto = CircleReqDto.builder()
                .name("대외활동")
                .alias("연합동아리")
                .unknown(false)
                .startdate(LocalDate.of(2023,5,1))
                .enddate(LocalDate.of(2023,12,12))
                .location(true)
                .role("운영진")
                .build();
        //when
        CircleResponse response = careerService.createCircle(requestMember,circleReqDto);
        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(3L),
                () -> assertThat(response.getName()).isEqualTo("대외활동"),
                () -> assertThat(response.getAlias()).isEqualTo("연합동아리"),
                () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2023,12,12)),
                () -> assertThat(response.getUnknown()).isEqualTo(false),
                () -> assertThat(response.getRole()).isEqualTo("운영진"),
                () -> assertThat(response.getLocation()).isEqualTo(true)

        );
    }
    @Test
    @DisplayName("[create] 새로운 Circle 만들기 - unknown값이 true일 경우 enddate값을 현재 날짜로 설정")
    void testCreateActivityWithUnknown(){
        //given
        CircleReqDto circleReqDto = CircleReqDto.builder()
                .name("대외활동")
                .alias("연합동아리")
                .unknown(true)
                .startdate(LocalDate.of(2023,5,1))
                .location(true)
                .role("운영진")
                .build();
        //when
        CircleResponse response = careerService.createCircle(requestMember,circleReqDto);

        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(3L),
                () -> assertThat(response.getName()).isEqualTo("대외활동"),
                () -> assertThat(response.getAlias()).isEqualTo("연합동아리"),
                () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(response.getUnknown()).isEqualTo(true),
                () -> assertThat(response.getRole()).isEqualTo("운영진"),
                () -> assertThat(response.getLocation()).isEqualTo(true)

        );
    }

    @Test
    @DisplayName("[update] 기존에 존재하는 Circle 수정하기 - unknown값이 true일 경우 endDate 값을 현재 날짜로 설정")
    void testUpdateCircleWithUnknown() {
        CircleReqDto circleReqDto = CircleReqDto.builder()
                .name("수정된 대외활동")
                .alias("수정된 연합동아리")
                .unknown(true)
                .startdate(LocalDate.of(2023,5,1))
                .location(true)
                .role("수정된 운영진")
                .build();
        //when
        CircleResponse updatedResponse = careerService.updateCircle(requestMember, 1L, circleReqDto);
        //then
        assertAll(
                () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
                () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 대외활동"),
                () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 연합동아리"),
                () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
                () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(updatedResponse.getUnknown()).isEqualTo(true),
                () -> assertThat(updatedResponse.getRole()).isEqualTo("수정된 운영진"),
                () -> assertThat(updatedResponse.getLocation()).isEqualTo(true)

        );

    }

    @Test
    @DisplayName("[update] 기존에 존재하는 Circle 수정하기 - 정상 요청")
    void testUpdateCircle() {
        //given
        CircleReqDto circleReqDto = CircleReqDto.builder()
                .name("수정된 대외활동")
                .alias("수정된 연합동아리")
                .unknown(false)
                .startdate(LocalDate.of(2023,5,1))
                .enddate(LocalDate.of(2023,6,1))
                .location(true)
                .role("수정된 운영진")
                .build();
        //when
        CircleResponse updatedResponse = careerService.updateCircle(requestMember, 1L, circleReqDto);
        //then
        assertAll(
                () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
                () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 대외활동"),
                () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 연합동아리"),
                () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
                () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.of(2023,6,1)),
                () -> assertThat(updatedResponse.getUnknown()).isEqualTo(false),
                () -> assertThat(updatedResponse.getRole()).isEqualTo("수정된 운영진"),
                () -> assertThat(updatedResponse.getLocation()).isEqualTo(true)

        );

    }


    @Test
    @DisplayName("[update] 기존에 존재하는 Circle 수정하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testUpdateCircleWithResourceNotFoundException() {
        //given
        CircleReqDto circleReqDto = CircleReqDto.builder()
                .name("수정된 대외활동")
                .alias("수정된 연합동아리")
                .unknown(false)
                .startdate(LocalDate.of(2023,5,1))
                .enddate(LocalDate.of(2023,6,1))
                .location(true)
                .role("수정된 운영진")
                .build();

        //when
        //then
        assertThatThrownBy(() -> careerService.updateCircle(requestMember, 999L, circleReqDto)).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    @DisplayName("[update] 기존에 존재하는 Circle 수정하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testUpdateCircleWithOwnerMismatchException() {
        //given
        CircleReqDto circleReqDto = CircleReqDto.builder()
                .name("수정된 대외활동")
                .alias("수정된 연합동아리")
                .unknown(false)
                .startdate(LocalDate.of(2023,5,1))
                .enddate(LocalDate.of(2023,6,1))
                .location(true)
                .role("수정된 운영진")
                .build();

        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.updateCircle(anotherMember, 1L, circleReqDto))
                .isInstanceOf(OwnerMismatchException.class);

    }

    @Test
    @DisplayName("[delete] 기존에 존재하는 Circle 제거하기 - 정상 요청")
    void testDeleteCircle() {
        //given
        Long circleId = 1L;
        //when
        careerService.deleteBaseCareer(requestMember,circleId,"circle");
        //then
        assertThat(circleRepository.findById(circleId)).isEmpty();

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Circle 제거하기 - 없는 타입으로의 요청일 경우 IllegalArgumentException 발생")
    void testDeleteCircleWithIllegalArgumentException() {
        //given
        Long circleId = 1L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, circleId, "noneType"))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Circle 제거하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testDeleteCircleWithResourceNotFoundException() {
        //given
        Long circleId = 9999L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, circleId, "circle"))
                .isInstanceOf(ResourceNotFoundException.class);

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Circle 제거하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testDeleteCircleWithOwnerMismatchException() {
        //given
        Long circleId = 1L;
        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(anotherMember, circleId, "circle"))
                .isInstanceOf(OwnerMismatchException.class);
    }
}
