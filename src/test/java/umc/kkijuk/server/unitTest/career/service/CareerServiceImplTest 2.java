package umc.kkijuk.server.unitTest.career.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import umc.kkijuk.server.career.controller.response.*;
import umc.kkijuk.server.career.domain.*;
import umc.kkijuk.server.career.dto.*;
import umc.kkijuk.server.career.repository.*;
import umc.kkijuk.server.career.service.CareerServiceImpl;
import umc.kkijuk.server.common.service.RecordUpdateManager;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.record.domain.Record;
import umc.kkijuk.server.record.repository.RecordRepository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class CareerServiceImplTest {

  @InjectMocks
  private CareerServiceImpl careerService;

  @Mock
  private ActivityRepository activityRepository;

  @Mock
  private CircleRepository circleRepository;

  @Mock
  private CompetitionRepository competitionRepository;

  @Mock
  private EduCareerRepository eduCareerRepository;

  @Mock
  private EmploymentRepository employmentRepository;

  @Mock
  private ProjectRepository projectRepository;

  @Mock
  private CareerEtcRepository etcRepository;

  @Mock
  private RecordRepository recordRepository;

  @Mock
  private RecordUpdateManager recordUpdateManager;

  @Test
  @DisplayName("[CreateCareer] Activity를 생성하면 활동이 저장되고 Record의 타임스탬프가 갱신되어야 한다.")
  void createActivity_shouldSaveActivityAndUpdateRecord() {
    Member member = Member.builder().id(1L).build();
    ActivityReqDto reqDto = ActivityReqDto.builder()
            .name("동아리 활동").unknown(false)
            .enddate(LocalDate.of(2023, 12, 31)).build();
    Record mockRecord = Record.builder().memberId(member.getId()).build();

    when(recordRepository.findByMemberId(member.getId())).thenReturn(mockRecord);
    when(activityRepository.save(any(Activity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ActivityResponse response = careerService.createActivity(member, reqDto);

    assertNotNull(response);
    assertEquals("동아리 활동", response.getName());
    verify(activityRepository).save(any(Activity.class));
    verify(recordUpdateManager).updateRecordTimestamp(mockRecord);
  }

  @Test
  @DisplayName("[CreateCareer] Circle을 생성하면 저장되고 Record의 타임스탬프가 갱신되어야 한다.")
  void createCircle_shouldSaveCircleAndUpdateRecord() {
    Member member = Member.builder().id(1L).build();
    CircleReqDto reqDto = CircleReqDto.builder()
            .name("소모임").unknown(false)
            .enddate(LocalDate.of(2023, 10, 10)).build();
    Record mockRecord = Record.builder().memberId(member.getId()).build();

    when(recordRepository.findByMemberId(member.getId())).thenReturn(mockRecord);
    when(circleRepository.save(any(Circle.class))).thenAnswer(invocation -> invocation.getArgument(0));

    CircleResponse response = careerService.createCircle(member, reqDto);

    assertNotNull(response);
    assertEquals("소모임", response.getName());
    verify(circleRepository).save(any(Circle.class));
    verify(recordUpdateManager).updateRecordTimestamp(mockRecord);
  }

  @Test
  @DisplayName("[CreateCareer] Competition을 생성하면 저장되고 Record의 타임스탬프가 갱신되어야 한다.")
  void createCompetition_shouldSaveCompetitionAndUpdateRecord() {
    Member member = Member.builder().id(1L).build();
    CompetitionReqDto reqDto = CompetitionReqDto.builder()
            .name("공모전").unknown(false)
            .enddate(LocalDate.of(2024, 1, 1)).build();
    Record mockRecord = Record.builder().memberId(member.getId()).build();

    when(recordRepository.findByMemberId(member.getId())).thenReturn(mockRecord);
    when(competitionRepository.save(any(Competition.class))).thenAnswer(invocation -> invocation.getArgument(0));

    CompetitionResponse response = careerService.createCompetition(member, reqDto);

    assertNotNull(response);
    assertEquals("공모전", response.getName());
    verify(competitionRepository).save(any(Competition.class));
    verify(recordUpdateManager).updateRecordTimestamp(mockRecord);
  }

  @Test
  @DisplayName("[CreateCareer] EduCareer를 생성하면 저장되고 Record의 타임스탬프가 갱신되어야 한다.")
  void createEduCareer_shouldSaveEduCareerAndUpdateRecord() {
    Member member = Member.builder().id(1L).build();
    EduCareerReqDto reqDto = EduCareerReqDto.builder()
            .name("교육 이력")
            .unknown(false)
            .enddate(LocalDate.of(2022, 5, 15))
            .time(120)
            .build();

    Record mockRecord = Record.builder().memberId(member.getId()).build();

    when(recordRepository.findByMemberId(member.getId())).thenReturn(mockRecord);
    when(eduCareerRepository.save(any(EduCareer.class))).thenAnswer(invocation -> invocation.getArgument(0));

    EduCareerResponse response = careerService.crateEduCareer(member, reqDto);

    assertNotNull(response);
    assertEquals("교육 이력", response.getName());
    verify(eduCareerRepository).save(any(EduCareer.class));
    verify(recordUpdateManager).updateRecordTimestamp(mockRecord);
  }

  @Test
  @DisplayName("[CreateCareer] Employment를 생성하면 저장되고 Record의 타임스탬프가 갱신되어야 한다.")
  void createEmployment_shouldSaveEmploymentAndUpdateRecord() {
    Member member = Member.builder().id(1L).build();
    EmploymentReqDto reqDto = EmploymentReqDto.builder()
            .name("직장").unknown(false)
            .enddate(LocalDate.of(2021, 8, 1)).build();
    Record mockRecord = Record.builder().memberId(member.getId()).build();

    when(recordRepository.findByMemberId(member.getId())).thenReturn(mockRecord);
    when(employmentRepository.save(any(Employment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    EmploymentResponse response = careerService.createEmployment(member, reqDto);

    assertNotNull(response);
    assertEquals("직장", response.getName());
    verify(employmentRepository).save(any(Employment.class));
    verify(recordUpdateManager).updateRecordTimestamp(mockRecord);
  }

  @Test
  @DisplayName("[CreateCareer] Project를 생성하면 저장되고 Record의 타임스탬프가 갱신되어야 한다.")
  void createProject_shouldSaveProjectAndUpdateRecord() {
    Member member = Member.builder().id(1L).build();
    ProjectReqDto reqDto = ProjectReqDto.builder()
            .name("사이드 프로젝트").unknown(false)
            .enddate(LocalDate.of(2024, 12, 1)).build();
    Record mockRecord = Record.builder().memberId(member.getId()).build();

    when(recordRepository.findByMemberId(member.getId())).thenReturn(mockRecord);
    when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ProjectResponse response = careerService.createProject(member, reqDto);

    assertNotNull(response);
    assertEquals("사이드 프로젝트", response.getName());
    verify(projectRepository).save(any(Project.class));
    verify(recordUpdateManager).updateRecordTimestamp(mockRecord);
  }

  @Test
  @DisplayName("[CreateCareer] Etc를 생성하면 저장되고 Record의 타임스탬프가 갱신되어야 한다.")
  void createEtc_shouldSaveEtcAndUpdateRecord() {
    Member member = Member.builder().id(1L).build();
    EtcReqDto reqDto = EtcReqDto.builder()
            .name("기타 활동").unknown(false)
            .enddate(LocalDate.of(2020, 2, 2)).build();
    Record mockRecord = Record.builder().memberId(member.getId()).build();

    when(recordRepository.findByMemberId(member.getId())).thenReturn(mockRecord);
    when(etcRepository.save(any(CareerEtc.class))).thenAnswer(invocation -> invocation.getArgument(0));

    EtcResponse response = careerService.createEtc(member, reqDto);

    assertNotNull(response);
    assertEquals("기타 활동", response.getName());
    verify(etcRepository).save(any(CareerEtc.class));
    verify(recordUpdateManager).updateRecordTimestamp(mockRecord);
  }

  @Test
  @DisplayName("[DeleteCareer] Activity를 삭제하면 Repository에서 제거되고 Record의 타임스탬프가 갱신되어야 한다.")
  void deleteActivity_shouldRemoveActivityAndUpdateRecord() {
    // given
    Long memberId = 1L;
    Long activityId = 100L;

    Member member = Member.builder().id(memberId).build();
    Activity activity = Activity.builder().memberId(memberId).build();
    setId(activity, activityId);
    Record mockRecord = Record.builder().memberId(memberId).build();

    when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
    when(recordRepository.findByMemberId(memberId)).thenReturn(mockRecord);

    // when
    careerService.deleteActivity(member, activityId);

    // then
    verify(activityRepository).delete(activity);
    verify(recordUpdateManager).updateRecordTimestamp(mockRecord);
  }

  @Test
  @DisplayName("[DeleteCareer] Circle을 삭제하면 Repository에서 제거되고 Record의 타임스탬프가 갱신되어야 한다.")
  void deleteCircle_shouldRemoveCircleAndUpdateRecord() {
    // given
    Long memberId = 1L;
    Long circleId = 101L;

    Member member = Member.builder().id(memberId).build();
    Circle circle = Circle.builder().memberId(memberId).build();
    setId(circle, circleId);
    Record record = Record.builder().memberId(memberId).build();

    when(circleRepository.findById(circleId)).thenReturn(Optional.of(circle));
    when(recordRepository.findByMemberId(memberId)).thenReturn(record);

    // when
    careerService.deleteCircle(member, circleId);

    // then
    verify(circleRepository).delete(circle);
    verify(recordUpdateManager).updateRecordTimestamp(record);
  }

  @Test
  @DisplayName("[DeleteCareer] Competition을 삭제하면 Repository에서 제거되고 Record의 타임스탬프가 갱신되어야 한다.")
  void deleteCompetition_shouldRemoveCompetitionAndUpdateRecord() {
    // given
    Long memberId = 1L;
    Long compId = 102L;

    Member member = Member.builder().id(memberId).build();
    Competition competition = Competition.builder().memberId(memberId).build();
    setId(competition, compId);
    Record record = Record.builder().memberId(memberId).build();

    when(competitionRepository.findById(compId)).thenReturn(Optional.of(competition));
    when(recordRepository.findByMemberId(memberId)).thenReturn(record);

    // when
    careerService.deleteComp(member, compId);

    // then
    verify(competitionRepository).delete(competition);
    verify(recordUpdateManager).updateRecordTimestamp(record);
  }

  @Test
  @DisplayName("[DeleteCareer] EduCareer를 삭제하면 Repository에서 제거되고 Record의 타임스탬프가 갱신되어야 한다.")
  void deleteEduCareer_shouldRemoveEduCareerAndUpdateRecord() {
    // given
    Long memberId = 1L;
    Long eduId = 103L;

    Member member = Member.builder().id(memberId).build();
    EduCareer edu = EduCareer.builder().memberId(memberId).build();
    setId(edu, eduId);
    Record record = Record.builder().memberId(memberId).build();

    when(eduCareerRepository.findById(eduId)).thenReturn(Optional.of(edu));
    when(recordRepository.findByMemberId(memberId)).thenReturn(record);

    // when
    careerService.deleteEdu(member, eduId);

    // then
    verify(eduCareerRepository).delete(edu);
    verify(recordUpdateManager).updateRecordTimestamp(record);
  }

  @Test
  @DisplayName("[DeleteCareer] Employment를 삭제하면 Repository에서 제거되고 Record의 타임스탬프가 갱신되어야 한다.")
  void deleteEmployment_shouldRemoveEmploymentAndUpdateRecord() {
    // given
    Long memberId = 1L;
    Long empId = 104L;

    Member member = Member.builder().id(memberId).build();
    Employment emp = Employment.builder().memberId(memberId).build();
    setId(emp, empId);
    Record record = Record.builder().memberId(memberId).build();

    when(employmentRepository.findById(empId)).thenReturn(Optional.of(emp));
    when(recordRepository.findByMemberId(memberId)).thenReturn(record);

    // when
    careerService.deleteEmp(member, empId);

    // then
    verify(employmentRepository).delete(emp);
    verify(recordUpdateManager).updateRecordTimestamp(record);
  }

  @Test
  @DisplayName("[DeleteCareer] Project를 삭제하면 Repository에서 제거되고 Record의 타임스탬프가 갱신되어야 한다.")
  void deleteProject_shouldRemoveProjectAndUpdateRecord() {
    // given
    Long memberId = 1L;
    Long projectId = 105L;

    Member member = Member.builder().id(memberId).build();
    Project project = Project.builder().memberId(memberId).build();
    setId(project, projectId);
    Record record = Record.builder().memberId(memberId).build();

    when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
    when(recordRepository.findByMemberId(memberId)).thenReturn(record);

    // when
    careerService.deleteProject(member, projectId);

    // then
    verify(projectRepository).delete(project);
    verify(recordUpdateManager).updateRecordTimestamp(record);
  }

  @Test
  @DisplayName("[DeleteCareer] Etc를 삭제하면 Repository에서 제거되고 Record의 타임스탬프가 갱신되어야 한다.")
  void deleteEtc_shouldRemoveEtcAndUpdateRecord() {
    // given
    Long memberId = 1L;
    Long etcId = 106L;

    Member member = Member.builder().id(memberId).build();
    CareerEtc etc = CareerEtc.builder().memberId(memberId).build();
    setId(etc, etcId);
    Record record = Record.builder().memberId(memberId).build();

    when(etcRepository.findById(etcId)).thenReturn(Optional.of(etc));
    when(recordRepository.findByMemberId(memberId)).thenReturn(record);

    // when
    careerService.deleteEtc(member, etcId);

    // then
    verify(etcRepository).delete(etc);
    verify(recordUpdateManager).updateRecordTimestamp(record);
  }

  private void setId(Object entity, Long id) {
    try {
      Field field = entity.getClass().getDeclaredField("id");
      field.setAccessible(true);
      field.set(entity, id);
    } catch (Exception e) {
      throw new RuntimeException("ID 주입 실패", e);
    }
  }


}

