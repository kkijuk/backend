package umc.kkijuk.server.unitTest.career.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.kkijuk.server.career.controller.response.EmploymentResponse;
import umc.kkijuk.server.career.domain.Employment;
import umc.kkijuk.server.career.domain.JobType;
import umc.kkijuk.server.career.dto.EmploymentReqDto;
import umc.kkijuk.server.career.repository.EmploymentRepository;
import umc.kkijuk.server.career.service.CareerService;
import umc.kkijuk.server.career.service.CareerServiceImpl;
import umc.kkijuk.server.common.domian.exception.OwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.unitTest.mock.FakeEmploymentRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class EmploymentServiceTest {
    private CareerService careerService;
    private final Long testMemberId = 3333L;
    private Member requestMember;

    //test Data
    private final LocalDate testStartDate = LocalDate.of(2023, 7, 19);
    private final LocalDate testEndDate = LocalDate.of(2023, 12, 19);
    //fake repository
    private EmploymentRepository employmentRepository;

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

        this.employmentRepository = new FakeEmploymentRepository();
        this.careerService = CareerServiceImpl.builder()
                .employmentRepository(employmentRepository)
                .build();

        Employment emp1 = Employment.builder()
                .memberId(testMemberId)
                .name("test emp")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .position("보조강사")
                .type(JobType.FULL_TIME)
                .alias("근무처")
                .field("마케팅")
                .build();

        Employment emp2 = Employment.builder()
                .memberId(testMemberId)
                .name("test emp")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .position("보조강사")
                .type(JobType.FREELANCE)
                .alias("근무처")
                .field("기획")
                .build();

        employmentRepository.save(emp1);
        employmentRepository.save(emp2);
    }
    @Test
    @DisplayName("[create] 새로운 Employment 만들기 - 정상 요청")
    void testCreateEmployment() {
        //given
        EmploymentReqDto employmentReqDto = EmploymentReqDto.builder()
                .name("경력")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .position("인턴")
                .type(JobType.FULL_TIME)
                .alias("근무처")
                .field("마케팅")
                .build();
        //when
        EmploymentResponse response = careerService.createEmployment(requestMember,employmentReqDto);
        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(3L),
                () -> assertThat(response.getName()).isEqualTo("경력"),
                () -> assertThat(response.getAlias()).isEqualTo("근무처"),
                () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2023,7,19)),
                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2023,12,19)),
                () -> assertThat(response.getUnknown()).isEqualTo(false),
                () -> assertThat(response.getType()).isEqualTo(JobType.FULL_TIME),
                () -> assertThat(response.getField()).isEqualTo("마케팅"),
                () -> assertThat(response.getPosition()).isEqualTo("인턴")

        );
    }
    @Test
    @DisplayName("[create] 새로운 Employment 만들기 - unknown값이 true일 경우 enddate값을 현재 날짜로 설정")
    void testCreateEmploymentWithUnknown(){
        //given
        EmploymentReqDto employmentReqDto = EmploymentReqDto.builder()
                .name("경력")
                .unknown(true)
                .startdate(testStartDate)
                .position("인턴")
                .type(JobType.FULL_TIME)
                .alias("근무처")
                .field("마케팅")
                .build();
        //when
        EmploymentResponse response = careerService.createEmployment(requestMember,employmentReqDto);
        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(3L),
                () -> assertThat(response.getName()).isEqualTo("경력"),
                () -> assertThat(response.getAlias()).isEqualTo("근무처"),
                () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2023,7,19)),
                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(response.getUnknown()).isEqualTo(true),
                () -> assertThat(response.getType()).isEqualTo(JobType.FULL_TIME),
                () -> assertThat(response.getField()).isEqualTo("마케팅"),
                () -> assertThat(response.getPosition()).isEqualTo("인턴")

        );
    }

    @Test
    @DisplayName("[update] 기존에 존재하는 Employment 수정하기 - unknown값이 true일 경우 endDate 값을 현재 날짜로 설정")
    void testUpdateEmploymentWithUnknown() {
        EmploymentReqDto employmentReqDto = EmploymentReqDto.builder()
                .name("수정된 경력")
                .unknown(true)
                .startdate(testStartDate)
                .position("수정된 인턴")
                .type(JobType.FREELANCE)
                .alias("수정된 근무처")
                .field("수정된 마케팅")
                .build();
        //when
        EmploymentResponse updatedResponse = careerService.updateEmp(requestMember,1L, employmentReqDto);
        //then
        assertAll(
                () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
                () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 경력"),
                () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 근무처"),
                () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2023,7,19)),
                () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(updatedResponse.getUnknown()).isEqualTo(true),
                () -> assertThat(updatedResponse.getType()).isEqualTo(JobType.FREELANCE),
                () -> assertThat(updatedResponse.getField()).isEqualTo("수정된 마케팅"),
                () -> assertThat(updatedResponse.getPosition()).isEqualTo("수정된 인턴")

        );
    }

    @Test
    @DisplayName("[update] 기존에 존재하는 Employment 수정하기 - 정상 요청")
    void testUpdateEmployment() {
        //given
        EmploymentReqDto employmentReqDto = EmploymentReqDto.builder()
                .name("수정된 경력")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .position("수정된 인턴")
                .type(JobType.FREELANCE)
                .alias("수정된 근무처")
                .field("수정된 마케팅")
                .build();
        //when
        EmploymentResponse updatedResponse = careerService.updateEmp(requestMember, 1L, employmentReqDto);
        //then
        //then
        assertAll(
                () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
                () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 경력"),
                () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 근무처"),
                () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2023,7,19)),
                () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.of(2023,12,19)),
                () -> assertThat(updatedResponse.getUnknown()).isEqualTo(false),
                () -> assertThat(updatedResponse.getType()).isEqualTo(JobType.FREELANCE),
                () -> assertThat(updatedResponse.getField()).isEqualTo("수정된 마케팅"),
                () -> assertThat(updatedResponse.getPosition()).isEqualTo("수정된 인턴")

        );

    }


    @Test
    @DisplayName("[update] 기존에 존재하는 Employment 수정하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testUpdateEmploymentWithResourceNotFoundException() {
        //given
        EmploymentReqDto employmentReqDto = EmploymentReqDto.builder()
                .name("수정된 경력")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .position("수정된 인턴")
                .type(JobType.FREELANCE)
                .alias("수정된 근무처")
                .field("수정된 마케팅")
                .build();

        //when
        //then
        assertThatThrownBy(() -> careerService.updateEmp(requestMember, 999L, employmentReqDto)).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    @DisplayName("[update] 기존에 존재하는 Employment 수정하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testUpdateEmploymentWithOwnerMismatchException() {
        //given
        EmploymentReqDto employmentReqDto = EmploymentReqDto.builder()
                .name("수정된 경력")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .position("수정된 인턴")
                .type(JobType.FREELANCE)
                .alias("수정된 근무처")
                .field("수정된 마케팅")
                .build();

        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.updateEmp(anotherMember, 1L, employmentReqDto))
                .isInstanceOf(OwnerMismatchException.class);

    }

    @Test
    @DisplayName("[delete] 기존에 존재하는 Employment 제거하기 - 정상 요청")
    void testDeleteEmployment() {
        //given
        Long empId = 1L;
        //when
        careerService.deleteBaseCareer(requestMember,empId,"employment");
        //then
        assertThat(employmentRepository.findById(empId)).isEmpty();

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Employment 제거하기 - 없는 타입으로의 요청일 경우 IllegalArgumentException 발생")
    void testDeleteEmploymentWithIllegalArgumentException() {
        //given
        Long empId = 1L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, empId, "noneType"))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Employment 제거하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testDeleteEmploymentWithResourceNotFoundException() {
        //given
        Long empId = 9999L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, empId, "employment"))
                .isInstanceOf(ResourceNotFoundException.class);

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Employment 제거하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testDeleteEmploymentWithOwnerMismatchException() {
        //given
        Long empId = 1L;
        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(anotherMember, empId, "employment"))
                .isInstanceOf(OwnerMismatchException.class);
    }
}
