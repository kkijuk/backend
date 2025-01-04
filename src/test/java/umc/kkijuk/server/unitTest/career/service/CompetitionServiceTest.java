package umc.kkijuk.server.unitTest.career.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.kkijuk.server.career.controller.response.CompetitionResponse;
import umc.kkijuk.server.career.domain.Competition;
import umc.kkijuk.server.career.dto.CompetitionReqDto;
import umc.kkijuk.server.career.repository.CompetitionRepository;
import umc.kkijuk.server.career.service.CareerService;
import umc.kkijuk.server.career.service.CareerServiceImpl;
import umc.kkijuk.server.common.domian.exception.OwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.unitTest.mock.FakeCompetitionRepository;

public class CompetitionServiceTest {
    private CareerService careerService;
    private final Long testMemberId = 3333L;
    private Member requestMember;

    //test Data
    private final LocalDate testStartDate = LocalDate.of(2023, 7, 19);
    private final LocalDate testEndDate = LocalDate.of(2023, 12, 19);
    //fake repository
    private CompetitionRepository competitionRepository;

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

        this.competitionRepository = new FakeCompetitionRepository();
        this.careerService = CareerServiceImpl.builder()
            .competitionRepository(competitionRepository)
            .build();

        Competition competition1 = Competition.builder()
            .memberId(testMemberId)
            .name("test competition")
            .alias("test alias")
            .unknown(false)
            .startdate(testStartDate)
            .enddate(testEndDate)
            .organizer("test organizer")
            .teamSize(10)
            .contribution(10)
            .build();

        Competition competition2 = Competition.builder()
            .memberId(testMemberId)
            .name("test competition")
            .alias("test alias")
            .unknown(false)
            .startdate(testStartDate)
            .enddate(testEndDate)
            .organizer("test organizer")
            .teamSize(10)
            .contribution(10)
            .build();

        competitionRepository.save(competition1);
        competitionRepository.save(competition2);
    }
    @Test
    @DisplayName("[create] 새로운 Competition 만들기 - 정상 요청")
    void testCreateCompetition() {
        //given
        CompetitionReqDto competitionReqDto = CompetitionReqDto.builder()
            .name("대회")
            .alias("대회 별칭")
            .unknown(false)
            .startdate(LocalDate.of(2023,5,1))
            .enddate(LocalDate.of(2023,12,12))
            .organizer("주최")
            .isTeam(true)
            .teamSize(10)
            .contribution(10)
            .build();
        //when
        CompetitionResponse response = careerService.createCompetition(requestMember,competitionReqDto);
        //then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(3L),
            () -> assertThat(response.getName()).isEqualTo("대회"),
            () -> assertThat(response.getAlias()).isEqualTo("대회 별칭"),
            () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
            () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2023,12,12)),
            () -> assertThat(response.getUnknown()).isEqualTo(false),
            () -> assertThat(response.getIsTeam()).isEqualTo(true),
            () -> assertThat(response.getOrganizer()).isEqualTo("주최"),
            () -> assertThat(response.getContribution()).isEqualTo(10),
            () -> assertThat(response.getTeamSize()).isEqualTo(10)
        );
    }
    @Test
    @DisplayName("[create] 새로운 Competition 만들기 - unknown값이 true일 경우 enddate값을 현재 날짜로 설정")
    void testCreateCompetitionWithUnknown(){
        //given
        CompetitionReqDto competitionReqDto = CompetitionReqDto.builder()
            .name("대회")
            .alias("대회 별칭")
            .unknown(true)
            .startdate(LocalDate.of(2023,5,1))
            .organizer("주최")
            .isTeam(true)
            .teamSize(10)
            .contribution(10)
            .build();
        //when
        CompetitionResponse response = careerService.createCompetition(requestMember,competitionReqDto);
        //then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(3L),
            () -> assertThat(response.getName()).isEqualTo("대회"),
            () -> assertThat(response.getAlias()).isEqualTo("대회 별칭"),
            () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
            () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.now()),
            () -> assertThat(response.getUnknown()).isEqualTo(true),
            () -> assertThat(response.getIsTeam()).isEqualTo(true),
            () -> assertThat(response.getContribution()).isEqualTo(10),
            () -> assertThat(response.getTeamSize()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("[update] 기존에 존재하는 Competition 수정하기 - unknown값이 true일 경우 endDate 값을 현재 날짜로 설정")
    void testUpdateCompetitionWithUnknown() {
        //given
        CompetitionReqDto competitionReqDto = CompetitionReqDto.builder()
            .name("수정된 대회")
            .alias("수정된 별칭")
            .unknown(true)
            .startdate(LocalDate.of(2023,5,1))
            .organizer("수정된 주최")
            .isTeam(true)
            .teamSize(10)
            .contribution(10)
            .build();
        //when
        CompetitionResponse updatedResponse = careerService.updateComp(requestMember, 1L, competitionReqDto);
        //then
        assertAll(
            () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
            () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 대회"),
            () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 별칭"),
            () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
            () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.now()),
            () -> assertThat(updatedResponse.getUnknown()).isEqualTo(true),
            () -> assertThat(updatedResponse.getIsTeam()).isEqualTo(true),
            () -> assertThat(updatedResponse.getOrganizer()).isEqualTo("수정된 주최"),
            () -> assertThat(updatedResponse.getTeamSize()).isEqualTo(10),
            () -> assertThat(updatedResponse.getContribution()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("[update] 기존에 존재하는 Competition 수정하기 - 정상 요청")
    void testUpdateCompetition() {
        //given
        CompetitionReqDto competitionReqDto = CompetitionReqDto.builder()
            .name("수정된 대회")
            .alias("수정된 별칭")
            .unknown(false)
            .startdate(LocalDate.of(2023,5,1))
            .enddate(LocalDate.of(2024,7,1))
            .organizer("수정된 주최")
            .isTeam(true)
            .teamSize(10)
            .contribution(10)
            .build();
        //when
        CompetitionResponse updatedResponse = careerService.updateComp(requestMember, 1L, competitionReqDto);
        //then
        assertAll(
            () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
            () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 대회"),
            () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 별칭"),
            () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
            () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.of(2024,7,1)),
            () -> assertThat(updatedResponse.getUnknown()).isEqualTo(false),
            () -> assertThat(updatedResponse.getIsTeam()).isEqualTo(true),
            () -> assertThat(updatedResponse.getOrganizer()).isEqualTo("수정된 주최"),
            () -> assertThat(updatedResponse.getTeamSize()).isEqualTo(10),
            () -> assertThat(updatedResponse.getContribution()).isEqualTo(10)

        );

    }


    @Test
    @DisplayName("[update] 기존에 존재하는 Competition 수정하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testUpdateCompetitionWithResourceNotFoundException() {
        //given
        CompetitionReqDto competitionReqDto = CompetitionReqDto.builder()
            .name("수정된 대회")
            .alias("수정된 별칭")
            .unknown(true)
            .startdate(LocalDate.of(2023,5,1))
            .enddate(LocalDate.of(2024,7,1))
            .organizer("수정된 주최")
            .isTeam(true)
            .teamSize(10)
            .contribution(10)
            .build();

        //when
        //then
        assertThatThrownBy(() -> careerService.updateComp(requestMember, 999L, competitionReqDto)).isInstanceOf(
            ResourceNotFoundException.class);
    }
    @Test
    @DisplayName("[update] 기존에 존재하는 Competition 수정하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testUpdateCompetitionWithOwnerMismatchException() {
        //given
        CompetitionReqDto competitionReqDto = CompetitionReqDto.builder()
            .name("수정된 대회")
            .alias("수정된 별칭")
            .unknown(true)
            .startdate(LocalDate.of(2023,5,1))
            .enddate(LocalDate.of(2024,7,1))
            .organizer("수정된 주최")
            .isTeam(true)
            .teamSize(10)
            .contribution(10)
            .build();

        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.updateComp(anotherMember, 1L, competitionReqDto))
            .isInstanceOf(OwnerMismatchException.class);

    }

    @Test
    @DisplayName("[delete] 기존에 존재하는 Competition 제거하기 - 정상 요청")
    void testDeleteCompetition() {
        //given
        Long compId = 1L;
        //when
        careerService.deleteBaseCareer(requestMember,compId,"competition");
        //then
        assertThat(competitionRepository.findById(compId)).isEmpty();

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Competition 제거하기 - 없는 타입으로의 요청일 경우 IllegalArgumentException 발생")
    void testDeleteCompetitionWithIllegalArgumentException() {
        //given
        Long compId = 1L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, compId, "noneType"))
            .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Competition 제거하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testDeleteCompetitionWithResourceNotFoundException() {
        //given
        Long compId = 9999L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, compId, "competition"))
            .isInstanceOf(ResourceNotFoundException.class);

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 v 제거하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testDeleteCompetitionWithOwnerMismatchException() {
        //given
        Long compId = 1L;
        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(anotherMember, compId, "competition"))
            .isInstanceOf(OwnerMismatchException.class);
    }
}
