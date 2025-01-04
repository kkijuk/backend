package umc.kkijuk.server.unitTest.career.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.kkijuk.server.career.controller.response.EduCareerResponse;
import umc.kkijuk.server.career.domain.EduCareer;
import umc.kkijuk.server.career.dto.EduCareerReqDto;
import umc.kkijuk.server.career.repository.EduCareerRepository;
import umc.kkijuk.server.career.service.CareerService;
import umc.kkijuk.server.career.service.CareerServiceImpl;
import umc.kkijuk.server.common.domian.exception.OwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.unitTest.mock.FakeEduCareerRepository;

public class EduCareerServiceTest {
    private CareerService careerService;
    private final Long testMemberId = 3333L;
    private Member requestMember;

    //test Data
    private final LocalDate testStartDate = LocalDate.of(2023, 7, 19);
    private final LocalDate testEndDate = LocalDate.of(2023, 12, 19);
    //fake repository
    private EduCareerRepository eduCareerRepository;

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

        this.eduCareerRepository = new FakeEduCareerRepository();
        this.careerService = CareerServiceImpl.builder()
            .eduCareerRepository(eduCareerRepository)
            .build();

        EduCareer eduCareer1 = EduCareer.builder()
            .memberId(testMemberId)
            .name("test competition")
            .alias("test alias")
            .unknown(false)
            .startdate(testStartDate)
            .enddate(testEndDate)
            .organizer("test organizer")
            .time(120)
            .build();

        EduCareer eduCareer2 = EduCareer.builder()
            .memberId(testMemberId)
            .name("test competition")
            .alias("test alias")
            .unknown(false)
            .startdate(testStartDate)
            .enddate(testEndDate)
            .organizer("test organizer")
            .time(120)
            .build();

        eduCareerRepository.save(eduCareer1);
        eduCareerRepository.save(eduCareer2);
    }
    @Test
    @DisplayName("[create] 새로운 EduCareer 만들기 - 정상 요청")
    void testCreateEduCareer() {
        //given
        EduCareerReqDto eduCareerReqDto = EduCareerReqDto.builder()
            .name("학습")
            .alias("학습 별칭")
            .unknown(false)
            .startdate(LocalDate.of(2023,5,1))
            .enddate(LocalDate.of(2023,12,12))
            .organizer("주최")
            .time(130)
            .build();
        //when
        EduCareerResponse response = careerService.crateEduCareer(requestMember,eduCareerReqDto);
        //then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(3L),
            () -> assertThat(response.getName()).isEqualTo("학습"),
            () -> assertThat(response.getAlias()).isEqualTo("학습 별칭"),
            () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
            () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2023,12,12)),
            () -> assertThat(response.getUnknown()).isEqualTo(false),
            () -> assertThat(response.getOrganizer()).isEqualTo("주최"),
            () -> assertThat(response.getTime()).isEqualTo(130)
        );
    }
    @Test
    @DisplayName("[create] 새로운 EduCareer 만들기 - unknown값이 true일 경우 enddate값을 현재 날짜로 설정")
    void testCreateEduCareerWithUnknown(){
        //given
        EduCareerReqDto eduCareerReqDto = EduCareerReqDto.builder()
            .name("학습")
            .alias("학습 별칭")
            .unknown(true)
            .startdate(LocalDate.of(2023,5,1))
            .organizer("주최")
            .time(130)
            .build();
        //when
        EduCareerResponse response = careerService.crateEduCareer(requestMember,eduCareerReqDto);
        //then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(3L),
            () -> assertThat(response.getName()).isEqualTo("학습"),
            () -> assertThat(response.getAlias()).isEqualTo("학습 별칭"),
            () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
            () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.now()),
            () -> assertThat(response.getUnknown()).isEqualTo(true),
            () -> assertThat(response.getOrganizer()).isEqualTo("주최"),
            () -> assertThat(response.getTime()).isEqualTo(130)
        );
    }

    @Test
    @DisplayName("[update] 기존에 존재하는 EduCareer 수정하기 - unknown값이 true일 경우 endDate 값을 현재 날짜로 설정")
    void testUpdateEduCareerWithUnknown() {
        //given
        EduCareerReqDto eduCareerReqDto = EduCareerReqDto.builder()
            .name("수정된 학습")
            .alias("수정된 학습 별칭")
            .unknown(true)
            .startdate(LocalDate.of(2023,5,1))
            .organizer("주최")
            .time(130)
            .build();
        //when
        EduCareerResponse updatedResponse = careerService.updateEdu(requestMember, 1L, eduCareerReqDto);
        //then
        assertAll(
            () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
            () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 학습"),
            () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 학습 별칭"),
            () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
            () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.now()),
            () -> assertThat(updatedResponse.getUnknown()).isEqualTo(true),
            () -> assertThat(updatedResponse.getOrganizer()).isEqualTo("주최"),
            () -> assertThat(updatedResponse.getTime()).isEqualTo(130)
        );
    }

    @Test
    @DisplayName("[update] 기존에 존재하는 EduCareer 수정하기 - 정상 요청")
    void testUpdateEduCareer() {
        //given
        EduCareerReqDto eduCareerReqDto = EduCareerReqDto.builder()
            .name("수정된 학습")
            .alias("수정된 학습 별칭")
            .unknown(false)
            .startdate(LocalDate.of(2023,5,1))
            .enddate(LocalDate.of(2023,12,1))
            .organizer("주최")
            .time(130)
            .build();
        //when
        EduCareerResponse updatedResponse = careerService.updateEdu(requestMember, 1L, eduCareerReqDto);
        //then
        assertAll(
            () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
            () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 학습"),
            () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 학습 별칭"),
            () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
            () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.of(2023,12,1)),
            () -> assertThat(updatedResponse.getUnknown()).isEqualTo(false),
            () -> assertThat(updatedResponse.getOrganizer()).isEqualTo("주최"),
            () -> assertThat(updatedResponse.getTime()).isEqualTo(130)
        );

    }


    @Test
    @DisplayName("[update] 기존에 존재하는 EduCareer 수정하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testUpdateEduCareerWithResourceNotFoundException() {
        //given
        EduCareerReqDto eduCareerReqDto = EduCareerReqDto.builder()
            .name("수정된 학습")
            .alias("수정된 학습 별칭")
            .unknown(false)
            .startdate(LocalDate.of(2023,5,1))
            .enddate(LocalDate.of(2023,12,1))
            .organizer("주최")
            .time(130)
            .build();

        //when
        //then
        assertThatThrownBy(() -> careerService.updateEdu(requestMember, 999L, eduCareerReqDto)).isInstanceOf(
            ResourceNotFoundException.class);
    }
    @Test
    @DisplayName("[update] 기존에 존재하는 EduCareer 수정하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testUpdateEduCareerWithOwnerMismatchException() {
        //given
        EduCareerReqDto eduCareerReqDto = EduCareerReqDto.builder()
            .name("수정된 학습")
            .alias("수정된 학습 별칭")
            .unknown(false)
            .startdate(LocalDate.of(2023,5,1))
            .enddate(LocalDate.of(2023,12,1))
            .organizer("주최")
            .time(130)
            .build();

        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.updateEdu(anotherMember, 1L, eduCareerReqDto))
            .isInstanceOf(OwnerMismatchException.class);

    }

    @Test
    @DisplayName("[delete] 기존에 존재하는 EduCareer 제거하기 - 정상 요청")
    void testDeleteCompetition() {
        //given
        Long eduId = 1L;
        //when
        careerService.deleteBaseCareer(requestMember,eduId,"edu");
        //then
        assertThat(eduCareerRepository.findById(eduId)).isEmpty();

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 EduCareer 제거하기 - 없는 타입으로의 요청일 경우 IllegalArgumentException 발생")
    void testDeleteEduCareerWithIllegalArgumentException() {
        //given
        Long eduId = 1L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, eduId, "noneType"))
            .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 EduCareer 제거하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testDeleteEduCareerWithResourceNotFoundException() {
        //given
        Long eduId = 9999L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, eduId, "edu"))
            .isInstanceOf(ResourceNotFoundException.class);

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 v 제거하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testDeleteEduCareerWithOwnerMismatchException() {
        //given
        Long eduId = 1L;
        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(anotherMember, eduId, "edu"))
            .isInstanceOf(OwnerMismatchException.class);
    }

}
