package umc.kkijuk.server.unitTest.career.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import umc.kkijuk.server.career.controller.response.*;
import umc.kkijuk.server.career.domain.*;
import umc.kkijuk.server.career.repository.*;
import umc.kkijuk.server.detail.domain.BaseCareerDetail;
import umc.kkijuk.server.detail.domain.CareerType;
import umc.kkijuk.server.detail.repository.CareerDetailRepository;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.tag.domain.Tag;
import umc.kkijuk.server.tag.repository.TagRepository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CareerSearchServiceImplTest {

    @InjectMocks
    private umc.kkijuk.server.career.service.CareerSearchServiceImpl careerSearchService;

    @Mock private ActivityRepository activityRepository;
    @Mock private CircleRepository circleRepository;
    @Mock private CompetitionRepository competitionRepository;
    @Mock private EduCareerRepository eduCareerRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private EmploymentRepository employmentRepository;
    @Mock private CareerEtcRepository etcRepository;
    @Mock private CareerDetailRepository detailRepository;
    @Mock private TagRepository tagRepository;

    private final Long memberId = 1L;
    private final Member member = Member.builder().id(memberId).build();

    @Test
    @DisplayName("[CareerSearch] 타임라인용 커리어 전체 조회 시 날짜 기준으로 정렬된 리스트가 반환되어야 한다.")
    void findCareerForTimeline_shouldReturnSortedTimelineResponses() {
        Activity activity = Activity.builder()
                .memberId(memberId).name("Activity").endDate(LocalDate.of(2024, 5, 1)).build();
        setId(activity, 1L);

        Competition competition = Competition.builder()
                .memberId(memberId).name("Competition").enddate(LocalDate.of(2023, 6, 15)).build();
        setId(competition, 2L);

        when(activityRepository.findByMemberId(memberId)).thenReturn(List.of(activity));
        when(competitionRepository.findByMemberId(memberId)).thenReturn(List.of(competition));
        when(circleRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(eduCareerRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(projectRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(employmentRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(etcRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());

        List<TimelineResponse> result = careerSearchService.findCareerForTimeline(member);

        assertEquals(2, result.size());
        assertEquals("Activity", result.get(0).getTitle());
    }

    @Test
    @DisplayName("[CareerSearch] 카테고리별 커리어 목록을 조회하면 분류된 Map이 반환되어야 한다.")
    void findAllCareerGroupedCategory_shouldReturnGroupedMap() {
        Activity activity = Activity.builder()
                .memberId(memberId).name("Act").startDate(LocalDate.of(2023, 1, 1)).build();
        setId(activity, 1L);

        when(activityRepository.findByMemberId(memberId)).thenReturn(List.of(activity));
        when(circleRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(competitionRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(eduCareerRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(projectRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(employmentRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(etcRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());

        Map<String, List<?>> result = careerSearchService.findAllCareerGroupedCategory(memberId);

        assertEquals(7, result.size());
        assertTrue(result.containsKey("대외활동"));
        assertEquals(1, result.get("대외활동").size());
    }

    @Test
    @DisplayName("[CareerSearch] 연도별 커리어 조회 시 시작년도 기준으로 그룹핑되어야 한다.")
    void findAllCareerGroupedYear_shouldGroupByStartYear() {
        Project project = Project.builder()
                .memberId(memberId).name("Project").startdate(LocalDate.of(2023, 2, 1)).build();
        setId(project, 1L);

        when(projectRepository.findByMemberId(memberId)).thenReturn(List.of(project));
        when(competitionRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(activityRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(circleRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(eduCareerRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(employmentRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(etcRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());

        Map<String, List<?>> result = careerSearchService.findAllCareerGroupedYear(memberId);

        assertTrue(result.containsKey("2023"));
        assertEquals(1, result.get("2023").size());
    }

    @Test
    @DisplayName("[CareerSearch] 전체 커리어 목록 조회 시 종료일 기준 내림차순 정렬되어야 한다.")
    void findAllCareer_shouldReturnSortedList() {
        Project project = Project.builder()
                .memberId(memberId).name("Project").enddate(LocalDate.of(2024, 3, 15)).build();
        setId(project, 1L);

        when(projectRepository.findByMemberId(memberId)).thenReturn(List.of(project));
        when(detailRepository.findByCareerIdAndCareerType(eq(CareerType.PROJECT), eq(1L))).thenReturn(Collections.emptyList());
        when(competitionRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(activityRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(circleRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(eduCareerRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(employmentRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(etcRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());

        List<BaseCareerResponse> result = careerSearchService.findAllCareer(memberId);

        assertEquals(1, result.size());
        ProjectResponse response = (ProjectResponse) result.get(0);
        assertEquals("Project", response.getName());
    }

    @Test
    @DisplayName("[CareerSearch] 활동 상세 조회 시 본인 소유 Activity가 존재하면 반환되어야 한다.")
    void findCareer_shouldReturnActivityResponse() {
        Long careerId = 10L;
        Activity activity = Activity.builder()
                .memberId(memberId).name("활동").build();
        setId(activity, careerId);

        when(activityRepository.findById(careerId)).thenReturn(Optional.of(activity));
        when(detailRepository.findByCareerIdAndCareerType(eq(CareerType.ACTIVITY), eq(careerId))).thenReturn(Collections.emptyList());

        BaseCareerResponse response = careerSearchService.findCareer(member, careerId, "activity");

        assertTrue(response instanceof ActivityResponse);
        ActivityResponse activityResponse = (ActivityResponse) response;
        assertEquals("활동", activityResponse.getName());
    }

    @Test
    @DisplayName("[CareerSearch] 키워드로 태그 검색 시 태그 목록과 연결된 상세 개수가 반환되어야 한다.")
    void findAllTag_shouldReturnTagsAndDetailCount() {
        Tag tag = Tag.builder().id(1L).memberId(memberId).name("keyword").build();
        BaseCareerDetail detail = mock(BaseCareerDetail.class);

        when(tagRepository.findByKeywordAndMemberId("keyword", memberId)).thenReturn(List.of(tag));
        when(detailRepository.findByTag(tag.getId())).thenReturn(List.of(detail));

        FindTagResponse.SearchTagResponse result = careerSearchService.findAllTag(member, "keyword");

        assertEquals(1, result.getTagList().size());
        assertEquals(1, result.getDetailCount());
    }

    @Test
    @DisplayName("[CareerSearch] 커리어 키워드 검색 시 이름에 키워드가 포함된 커리어가 정렬되어 반환되어야 한다.")
    void findCareerWithKeyword_shouldReturnFilteredList() {
        Activity act = Activity.builder()
                .memberId(memberId).name("검색활동").startDate(LocalDate.of(2023, 3, 1)).build();
        setId(act, 1L);

        when(activityRepository.findByMemberIdAndNameContaining(eq(memberId), anyString())).thenReturn(List.of(act));
        when(competitionRepository.findByMemberIdAndNameContaining(eq(memberId), anyString())).thenReturn(Collections.emptyList());
        when(circleRepository.findByMemberIdAndNameContaining(eq(memberId), anyString())).thenReturn(Collections.emptyList());
        when(eduCareerRepository.findByMemberIdAndNameContaining(eq(memberId), anyString())).thenReturn(Collections.emptyList());
        when(projectRepository.findByMemberIdAndNameContaining(eq(memberId), anyString())).thenReturn(Collections.emptyList());
        when(employmentRepository.findByMemberIdAndNameContaining(eq(memberId), anyString())).thenReturn(Collections.emptyList());
        when(etcRepository.findByMemberIdAndNameContaining(eq(memberId), anyString())).thenReturn(Collections.emptyList());

        List<FindCareerResponse> result = careerSearchService.findCareerWithKeyword(member, "검색", "new");

        assertEquals(1, result.size());
        assertEquals("검색활동", result.get(0).getCareerTitle());
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

