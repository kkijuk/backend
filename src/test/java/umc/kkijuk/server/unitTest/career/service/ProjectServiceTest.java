package umc.kkijuk.server.unitTest.career.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.kkijuk.server.career.controller.response.ProjectResponse;
import umc.kkijuk.server.career.domain.Project;
import umc.kkijuk.server.career.domain.ProjectType;
import umc.kkijuk.server.career.dto.ProjectReqDto;
import umc.kkijuk.server.career.repository.ProjectRepository;
import umc.kkijuk.server.career.service.CareerService;
import umc.kkijuk.server.career.service.CareerServiceImpl;
import umc.kkijuk.server.common.domian.exception.OwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.unitTest.mock.FakeProjectRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProjectServiceTest {
    private CareerService careerService;
    private final Long testMemberId = 3333L;
    private Member requestMember;

    //test Data
    private final LocalDate testStartDate = LocalDate.of(2023, 7, 19);
    private final LocalDate testEndDate = LocalDate.of(2023, 12, 19);
    //fake repository
    private ProjectRepository projectRepository;

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

        this.projectRepository = new FakeProjectRepository();
        this.careerService = CareerServiceImpl.builder()
                .projectJpaRepository(projectRepository)
                .build();

        Project project1 = Project.builder()
                .memberId(testMemberId)
                .name("test project")
                .alias("test alias")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .teamSize(10)
                .isTeam(true)
                .contribution(20)
                .location(ProjectType.ON_CAMPUS)
                .build();

        Project project2 = Project.builder()
                .memberId(testMemberId)
                .name("test project")
                .alias("test alias")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .teamSize(10)
                .isTeam(true)
                .contribution(20)
                .location(ProjectType.ON_CAMPUS)
                .build();

        projectRepository.save(project1);
        projectRepository.save(project2);
    }
    @Test
    @DisplayName("[create] 새로운 Project 만들기 - 정상 요청")
    void testCreateProject() {
        //given
        ProjectReqDto projectReqDto = ProjectReqDto.builder()
                .name("프로젝트")
                .alias("끼적")
                .startdate(LocalDate.of(2024,7,1))
                .enddate(LocalDate.of(2025,1,1))
                .isTeam(true)
                .unknown(false)
                .location(ProjectType.OFF_CAMPUS)
                .contribution(30)
                .teamSize(10)
                .build();
        //when
        ProjectResponse response = careerService.createProject(requestMember,projectReqDto);
        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(3L),
                () -> assertThat(response.getName()).isEqualTo("프로젝트"),
                () -> assertThat(response.getAlias()).isEqualTo("끼적"),
                () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2024,7,1)),
                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2025,1,1)),
                () -> assertThat(response.getIsTeam()).isEqualTo(true),
                () -> assertThat(response.getUnknown()).isEqualTo(false),
                () -> assertThat(response.getLocation()).isEqualTo(ProjectType.OFF_CAMPUS),
                () -> assertThat(response.getContribution()).isEqualTo(30),
                () -> assertThat(response.getTeamSize()).isEqualTo(10)

        );
    }
    @Test
    @DisplayName("[create] 새로운 Project 만들기 - unknown값이 true일 경우 enddate 값을 현재 날짜로 설정")
    void testCreateProjectIWithUnknown(){
        //given
        ProjectReqDto projectReqDto = ProjectReqDto.builder()
                .name("프로젝트")
                .alias("끼적")
                .startdate(LocalDate.of(2024,7,1))
                .isTeam(true)
                .unknown(true)
                .location(ProjectType.OFF_CAMPUS)
                .contribution(30)
                .teamSize(10)
                .build();
        //when
        ProjectResponse response = careerService.createProject(requestMember,projectReqDto);
        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(3L),
                () -> assertThat(response.getName()).isEqualTo("프로젝트"),
                () -> assertThat(response.getAlias()).isEqualTo("끼적"),
                () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2024,7,1)),
                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(response.getIsTeam()).isEqualTo(true),
                () -> assertThat(response.getUnknown()).isEqualTo(true),
                () -> assertThat(response.getLocation()).isEqualTo(ProjectType.OFF_CAMPUS),
                () -> assertThat(response.getContribution()).isEqualTo(30),
                () -> assertThat(response.getTeamSize()).isEqualTo(10)

        );
    }

    @Test
    @DisplayName("[update] 기존에 존재하는 Project 수정하기 - unknown값이 true일 경우 현재 날짜로 설정")
    void testUpdateProjectWithUnknown() {
        //given
        ProjectReqDto projectReqDto = ProjectReqDto.builder()
                .name("수정된 프로젝트")
                .alias("수정된 끼적")
                .startdate(LocalDate.of(2021,7,1))
                .isTeam(true)
                .unknown(true)
                .location(ProjectType.ON_CAMPUS)
                .contribution(30)
                .teamSize(10)
                .build();
        //when

        //when
        ProjectResponse updatedResponse = careerService.updateProject(requestMember, 1L, projectReqDto);
        //then
        assertAll(
                () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
                () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 프로젝트"),
                () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 끼적"),
                () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2021,7,1)),
                () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(updatedResponse.getIsTeam()).isEqualTo(true),
                () -> assertThat(updatedResponse.getUnknown()).isEqualTo(true),
                () -> assertThat(updatedResponse.getLocation()).isEqualTo(ProjectType.ON_CAMPUS),
                () -> assertThat(updatedResponse.getContribution()).isEqualTo(30),
                () -> assertThat(updatedResponse.getTeamSize()).isEqualTo(10)

        );

    }

    @Test
    @DisplayName("[update] 기존에 존재하는 Project 수정하기 - 정상 요청")
    void testUpdateProject() {
        //given
        ProjectReqDto projectReqDto = ProjectReqDto.builder()
                .name("수정된 프로젝트")
                .alias("수정된 끼적")
                .startdate(LocalDate.of(2021,7,1))
                .enddate(LocalDate.of(2021,8,1))
                .isTeam(true)
                .unknown(false)
                .location(ProjectType.ON_CAMPUS)
                .contribution(30)
                .teamSize(10)
                .build();
        //when

        //when
        ProjectResponse updatedResponse = careerService.updateProject(requestMember, 1L, projectReqDto);
        //then
        assertAll(
                () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
                () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 프로젝트"),
                () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 끼적"),
                () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2021,7,1)),
                () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.of(2021,8,1)),
                () -> assertThat(updatedResponse.getIsTeam()).isEqualTo(true),
                () -> assertThat(updatedResponse.getUnknown()).isEqualTo(false),
                () -> assertThat(updatedResponse.getLocation()).isEqualTo(ProjectType.ON_CAMPUS),
                () -> assertThat(updatedResponse.getContribution()).isEqualTo(30),
                () -> assertThat(updatedResponse.getTeamSize()).isEqualTo(10)

        );

    }


    @Test
    @DisplayName("[update] 기존에 존재하는 Project 수정하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testUpdateProjectWithResourceNotFoundException() {
        //given
        ProjectReqDto projectReqDto = ProjectReqDto.builder()
                .name("수정된 프로젝트")
                .alias("수정된 끼적")
                .startdate(LocalDate.of(2021,7,1))
                .enddate(LocalDate.of(2021,8,1))
                .isTeam(true)
                .unknown(false)
                .location(ProjectType.ON_CAMPUS)
                .contribution(30)
                .teamSize(10)
                .build();

        //when
        //then
        assertThatThrownBy(() -> careerService.updateProject(requestMember, 999L, projectReqDto)).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    @DisplayName("[update] 기존에 존재하는 Project 수정하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testUpdateProjectWithOwnerMismatchException() {
        //given
        ProjectReqDto projectReqDto = ProjectReqDto.builder()
                .name("수정된 프로젝트")
                .alias("수정된 끼적")
                .startdate(LocalDate.of(2021,7,1))
                .enddate(LocalDate.of(2021,8,1))
                .isTeam(true)
                .unknown(false)
                .location(ProjectType.ON_CAMPUS)
                .contribution(30)
                .teamSize(10)
                .build();

        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.updateProject(anotherMember, 1L, projectReqDto))
                .isInstanceOf(OwnerMismatchException.class);

    }

    @Test
    @DisplayName("[delete] 기존에 존재하는 Project 제거하기 - 정상 요청")
    void testDeleteProject() {
        //given
        Long projectId = 1L;
        //when
        careerService.deleteBaseCareer(requestMember,projectId,"project");
        //then
        assertThat(projectRepository.findById(projectId)).isEmpty();

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Project 제거하기 - 없는 타입으로의 요청일 경우 IllegalArgumentException 발생")
    void testDeleteProjectWithIllegalArgumentException() {
        //given
        Long projectId = 1L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, projectId, "noneType"))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Project 제거하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testDeleteProjectWithResourceNotFoundException() {
        //given
        Long projectId = 9999L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, projectId, "project"))
                .isInstanceOf(ResourceNotFoundException.class);

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 Project 제거하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testDeleteProjectWithOwnerMismatchException() {
        //given
        Long projectId = 1L;
        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(anotherMember, projectId, "project"))
                .isInstanceOf(OwnerMismatchException.class);
    }

}
